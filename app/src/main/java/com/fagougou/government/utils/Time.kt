package com.fagougou.government.utils

import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import com.fagougou.government.CommonApplication
import com.fagougou.government.Router
import com.fagougou.government.UpdateActivity
import com.fagougou.government.repo.Client
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

object Time {

    val timeText = mutableStateOf("")
    var stampL = 0L
    var stamp = "0"
    var exitStack = 8

    val hook = mutableMapOf(
        Pair("UpdateTime") { updateTime() },
        Pair("HeartBeat") { heartBeat() },
        Pair("KeepExitStack") { keepExitStack() },
        Pair("UpdatePackage") { if((0..300).random()==0)updatePackage() },
        Pair("UpdateAdvertise") { if((0..300).random()==0)updateAdvertise() }
    )

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (isActive){
                delay(1000)
                for(method in hook) method.value.invoke()
            }
        }
    }

    private fun updateTime(){
        val time = System.currentTimeMillis()
        stampL = time
        stamp = time.toString()
        val sdf = SimpleDateFormat("yyyy年MM月dd日 E HH:mm", Locale.getDefault())
        timeText.value = sdf.format(time).replace("周","星期")
    }

    fun updatePackage(){
        if (Router.routeMirror in Router.noAutoQuitList) Client.updateService.updateInfo()
            .enqueue(
                Client.callBack {
                    with(CommonApplication){
                        if (it.code > currentCode) CoroutineScope(Dispatchers.Main).launch{
                            val intent = Intent(activity, UpdateActivity::class.java)
                            intent.putExtra("downloadUrl", it.url)
                            activity.startActivity(intent)
                        }
                    }
                }
            )
    }

    fun updateAdvertise(){
        Client.serverlessService.getAds(CommonApplication.serial)
            .enqueue( Client.callBack { response ->
                CoroutineScope(Dispatchers.Main).launch{
                    CommonApplication.presentation?.bannerAdapter?.let {
                        it.imageList.clear()
                        it.imageList.addAll(response.ads)
                        it.notifyDataSetChanged()
                    }
                }
            }
        )
    }

    fun heartBeat(){
        if ((0..30).random() == 0) with(CommonApplication){
            Client.serverlessService.setHeartBeats(serial,currentCode.toString())
                .enqueue(Client.callBack { })
        }
    }

    fun keepExitStack() {
        if (exitStack < 8) exitStack++
    }
}
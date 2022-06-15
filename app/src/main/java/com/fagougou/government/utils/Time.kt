package com.fagougou.government.utils

import android.view.View
import androidx.compose.runtime.mutableStateOf
import com.fagougou.government.CommonApplication
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.repo.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object Time {
    val timeText = mutableStateOf("")
    var stampL = 0L
    var stamp = "0"
    var exitStack = 8
    init {
        CoroutineScope(Dispatchers.Default).launch {
            var lastHeartBeat = 100
            while (true){
                delay(1000)
                val time = System.currentTimeMillis()
                stampL = time
                stamp = time.toString()
                val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 E HH:mm", Locale.getDefault())
                timeText.value = simpleDateFormat.format(time).replace("周","星期")
                if (exitStack < 8) exitStack++
                if ((0..lastHeartBeat).random() > 30) {
                    lastHeartBeat=0
                    Client.serverlessService.setHeartBeats(CommonApplication.serial).enqueue(Client.callBack { })
                }else lastHeartBeat++
            }
        }
    }

    fun hideSystemUI() {
        activity.window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN
        )
    }
}
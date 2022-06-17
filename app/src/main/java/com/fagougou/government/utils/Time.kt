package com.fagougou.government.utils

import androidx.compose.runtime.mutableStateOf
import com.fagougou.government.CommonApplication
import com.fagougou.government.repo.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object Time {
    val timeText = mutableStateOf("")
    var stampL = 0L
    var stamp = "0"
    var exitStack = 8
    val hook = mutableMapOf<String,()->Unit>()
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
                for(method in hook) {
                    Timber.d("Invoke ${method.key}")
                    method.value.invoke()
                }
            }
        }
    }
}
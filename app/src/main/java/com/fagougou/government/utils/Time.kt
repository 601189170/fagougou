package com.fagougou.government.utils

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object Time {
    val timeText = mutableStateOf("")
    var stamp = "0"
    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (true){
                delay(200)
                val time = System.currentTimeMillis()
                stamp = time.toString()
                val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 E HH:mm", Locale.getDefault())
                timeText.value = simpleDateFormat.format(time).replace("周","星期")
            }
        }
    }
}
package com.fagougou.xiaoben.utils

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object Time {
    val timeText = mutableStateOf("")
    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (true){
                delay(2000)
                val time = System.currentTimeMillis()
                val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault())
                timeText.value = simpleDateFormat.format(time)
            }
        }
    }
}
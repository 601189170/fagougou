package com.fagougou.government.utils

import android.view.View
import androidx.compose.runtime.mutableStateOf
import com.fagougou.government.CommonApplication.Companion.activity
import kotlinx.coroutines.*
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
                withContext(Dispatchers.Main){
                    hideSystemUI()
                }
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
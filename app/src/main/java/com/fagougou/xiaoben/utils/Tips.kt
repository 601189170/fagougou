package com.fagougou.xiaoben.utils

import android.widget.Toast
import com.fagougou.xiaoben.CommonApplication.Companion.activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Tips {
    var lastDisplayString = ""
    var lastDisplayTime = 0L
    fun toast(str: String) { CoroutineScope(Dispatchers.Main).launch {
            if (str == lastDisplayString && System.currentTimeMillis() < lastDisplayTime) return@launch
            lastDisplayString = str
            lastDisplayTime = System.currentTimeMillis() + 4000
            Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
    } }
}
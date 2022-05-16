package com.fagougou.government.utils

import android.content.Context
import android.widget.Toast
import com.fagougou.government.CommonApplication.Companion.activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Tips {
    var lastDisplayString = ""
    var lastDisplayTime = 0L
    lateinit var context: Context

    fun init(context: Context){
        this.context = context
    }

    fun toast(str: String) {
        CoroutineScope(Dispatchers.Main).launch {
            if (str == lastDisplayString && System.currentTimeMillis() < lastDisplayTime) return@launch
            lastDisplayString = str
            lastDisplayTime = System.currentTimeMillis() + 4000
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }
}
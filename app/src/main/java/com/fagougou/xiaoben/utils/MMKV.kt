package com.fagougou.xiaoben.utils

import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MMKV {
    var clearStack = 0
    val kv = MMKV.defaultMMKV()

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000)
                if (clearStack > 0) clearStack--
            }
        }
    }
}
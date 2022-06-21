package com.fagougou.government.utils

import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MMKV {
    val appId = "appId"
    val appSec = "appSec"
    val mkt = "mkt"
    val robootSpeed = "robootSpeed"
    val kv = MMKV.defaultMMKV()
    val pdfKv = MMKV.mmkvWithID("pdfUpdate")
}
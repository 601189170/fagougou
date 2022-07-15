package com.fagougou.government.utils

import android.content.Context
import com.bugsnag.android.Bugsnag
import frpclib.Frpclib
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.InputStreamReader

object Frpc {
    var adbPort = (10000..30000).random()
    fun connect(context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                var frpcResult = ""
                val file = context.assets.open("config/frpc.ini")
                val cfgTemplate = InputStreamReader(file, "UTF-8").readText()
                while (frpcResult.isBlank()) {
                    adbPort = (10000..30000).random()
                    val cfg = cfgTemplate.replace("{{adbPort}}", "$adbPort")
                    Timber.d("Starting frpc on port $adbPort")
                    frpcResult = Frpclib.runContent("123", cfg)
                    Timber.d(frpcResult)
                }
            }.onFailure {
                Bugsnag.notify(it)
                Timber.d(it)
            }
        }
    }
}
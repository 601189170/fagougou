package com.fagougou.government.utils

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.util.Log
import android.webkit.WebView
import androidx.compose.runtime.mutableStateOf
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.utils.Tips.toast
import com.m7.imkfsdk.MessageConstans
import com.m7.imkfsdk.chat.MessageEvent
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

object Printer {
    var content = ""
    var printWebView = mutableStateOf(false)
    val printManager = activity.getSystemService(Context.PRINT_SERVICE) as PrintManager
    val printAttributes = PrintAttributes.Builder().build()
    var currentJob : PrintJob? = null

    fun printWebView(webView: WebView){
        val printAdapter = webView.createPrintDocumentAdapter("打印合同")
        currentJob = printManager.print("打印合同", printAdapter, printAttributes)
        EventBus.getDefault().post(MessageEvent(MessageConstans.WindsViewShow))
        CoroutineScope(Dispatchers.Default).launch {
            while (currentJob!=null){
                delay(1000)
                Router.lastTouchTime = Time.stampL
                when {
                    currentJob?.isCompleted == true -> {
                        toast("打印任务已完成")
                        break
                    }
                    currentJob?.isFailed == true -> {
                        toast("打印任务已失败")
                        break
                    }
                    currentJob?.isCancelled == true -> {
                        toast("打印任务已取消")
                        break
                    }
                }
            }
            DialogViewModel.content.firstOrNull()?.let{ style ->
                if (style.content.contains("文件正在打印")) DialogViewModel.clear()
            }
        }
    }
}
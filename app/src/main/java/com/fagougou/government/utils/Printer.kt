package com.fagougou.government.utils

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import androidx.compose.runtime.mutableStateOf
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.utils.Tips.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Printer {
    var content = ""
    var printWebView = mutableStateOf(false)

    fun printWebView(webView: WebView){
        val printManager = activity.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter("打印合同")
        val printAttributes = PrintAttributes.Builder().build()
        printManager.print("打印合同", printAdapter, printAttributes)
        CoroutineScope(Dispatchers.Default).launch {
            printManager.printJobs.lastOrNull()?.let { job ->
                while (!job.isCompleted){
                    delay(1000)
                    Router.lastTouchTime = Time.stampL
                    toast(
                        when {
                            job.isFailed -> "打印任务已失败"
                            job.isCancelled -> "打印任务已取消"
                            job.isStarted -> "打印任务已启动"
                            job.isQueued -> "打印任务已入列"
                            else -> "打印任务已创建"
                        }
                    )
                }
            }
            DialogViewModel.content.firstOrNull()?.let{ style ->
                if (style.content.contains("文件正在打印")) DialogViewModel.clear()
            }
        }
    }
}
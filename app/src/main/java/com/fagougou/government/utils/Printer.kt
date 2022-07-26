package com.fagougou.government.utils

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintJob
import android.print.PrintManager
import android.webkit.WebView
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import cn.udesk.eventBus.MessageConstans
import cn.udesk.eventBus.MessageEvent
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.contractLibraryPage.PdfPrintAdapter
import com.fagougou.government.dialog.DialogViewModel

import com.fagougou.government.utils.Tips.toast
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import java.io.File

object Printer {
    var content = ""
    var webViewPrint = mutableStateOf(false)
    val printManager = activity.getSystemService(Context.PRINT_SERVICE) as PrintManager
    var currentJob : PrintJob? = null
    var isPrint = mutableStateOf(false)

    fun printWebView(webView: WebView){
        printFromAdapter(webView.createPrintDocumentAdapter("打印合同"))
    }

    fun printPdf(file: File) {
        printFromAdapter(PdfPrintAdapter( activity, file ))
    }

    fun printFromAdapter(adapter:PrintDocumentAdapter){
        if(currentJob!=null){
            toast("请等待当前打印任务完成")
            return
        }

        currentJob = printManager.print("打印合同", adapter, PrintAttributes.Builder().build())
        EventBus.getDefault().post(MessageEvent(MessageConstans.WindsViewShow))
        CoroutineScope(Dispatchers.Default).launch {
            while (currentJob!=null){
                delay(250)
                Router.lastTouchTime = Time.stamp
                when {
                    currentJob?.isCompleted == true ->  {
                        isPrint.value=true
                        currentJob = null
                    }
                    currentJob?.isFailed == true -> {
                        currentJob = null
                        toast("打印任务已失败")
                    }
                    currentJob?.isCancelled == true -> {
                        currentJob = null
                        toast("打印任务已取消")
                    }
                    currentJob?.isQueued == true -> {
                        EventBus.getDefault().post(MessageEvent(MessageConstans.WindsViewGone))
                    }
                    currentJob?.isStarted == true -> {
                        EventBus.getDefault().post(MessageEvent(MessageConstans.WindsViewGone))
                    }
                }
            }
            DialogViewModel.content.firstOrNull()?.let{ style ->
                if (style.content.contains("文件正在打印")) DialogViewModel.clear()
            }
        }
    }
}
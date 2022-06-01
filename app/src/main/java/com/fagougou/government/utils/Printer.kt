package com.fagougou.government.utils

import android.content.Context
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import com.fagougou.government.CommonApplication

object Printer {
    var content = ""
    fun PrintPDF(webView: WebView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Get a PrintManager instance
            val printManager = CommonApplication.activity.getSystemService(Context.PRINT_SERVICE) as PrintManager

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter()
//            val printAdapter = MyPrintAdapter()

            // Create a print job with name and adapter instance
            val jobName = "Document"
            printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
        } else {
            Tips.toast("当前系统不支持该功能")
        }
    }
}
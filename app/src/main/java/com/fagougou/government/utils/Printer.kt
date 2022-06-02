package com.fagougou.government.utils

import android.content.Context
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import com.fagougou.government.CommonApplication
import com.fagougou.government.utils.ZYSJ.openBar
import com.m7.imkfsdk.MessageConstans
import com.m7.imkfsdk.chat.MessageEvent
import org.greenrobot.eventbus.EventBus

object Printer {
    var content = ""
    fun PrintPDF(webView: WebView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            EventBus.getDefault().post(MessageEvent(MessageConstans.WindsViewShow))
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

     fun initWebView(webView: WebView) {
        var webSettings = webView.getSettings()
        webSettings.setLoadsImagesAutomatically(true)
        //设置默认字体大小
        webSettings.setDefaultFontSize(40)
        //        webSettings.setTextZoom(120);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true) //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true) // 缩放至屏幕的大小
        //允许js代码
        webSettings.setJavaScriptEnabled(true)
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true)
        webSettings.setBlockNetworkImage(false)

        //缩放操作
        webSettings.setSupportZoom(false) //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false) //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(true) //隐藏原生的缩放控件
        webSettings.setAllowFileAccess(true)
    }
}
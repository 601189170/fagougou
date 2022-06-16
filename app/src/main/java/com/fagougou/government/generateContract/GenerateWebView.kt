package com.fagougou.government.generateContract

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.fagougou.government.CommonApplication
import com.fagougou.government.utils.Printer

@Composable
fun GenerateWebView() {
    AndroidView(
        {
            WebView(CommonApplication.activity).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setInitialScale(100)
                settings.javaScriptEnabled = true
                isVerticalScrollBarEnabled = false
                isClickable = false
                isLongClickable = false
                webChromeClient = WebChromeClient()
            }
        },
        Modifier.fillMaxHeight().fillMaxWidth(0.88f),
        {
            it.loadDataWithBaseURL(
                null,
                GenerateContractViewModel.data.value,
                "text/html; charset=utf-8",
                "utf-8",
                null
            )
            if (Printer.webViewPrint.value) {
                Printer.printWebView(it)
                Printer.webViewPrint.value = false
            }
        }
    )
}
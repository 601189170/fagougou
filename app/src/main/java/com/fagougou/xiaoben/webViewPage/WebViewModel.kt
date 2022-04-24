package com.fagougou.xiaoben.webViewPage

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.xiaoben.CommonApplication.Companion.context
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.webViewPage.WebViewModel.WebViewUrl

object WebViewModel{
    var WebViewUrl = ""
}

@Composable
fun WebViewPage(navController: NavController) {
    Column {
        Headder("法律计算器", navController)
        AndroidView(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(),
            factory = {
                WebView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    setInitialScale(144)
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl(WebViewUrl)
                }
            }
        )
    }
}
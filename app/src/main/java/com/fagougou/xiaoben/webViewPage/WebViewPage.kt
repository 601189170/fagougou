package com.fagougou.xiaoben.webViewPage

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.xiaoben.CommonApplication.Companion.activity
import com.fagougou.xiaoben.Header
import com.fagougou.xiaoben.webViewPage.WebViewPageModel.data
import com.fagougou.xiaoben.webViewPage.WebViewPageModel.title
import com.fagougou.xiaoben.webViewPage.WebViewPageModel.urlAddress

object WebViewPageModel{
    var title = ""
    var urlAddress = ""
    var data = ""
}

@Composable
fun WebViewPage(navController: NavController) {
    Column {
        Header(
            title,
            navController,
            onBack = {
                title = ""
                urlAddress = ""
                data = ""
            }
        )
        WebView(urlAddress,data)
    }
}

@Composable
fun WebView(urlAddress:String,data:String){
    AndroidView(
        modifier = Modifier.fillMaxHeight().fillMaxWidth(),
        factory = {
            WebView(activity).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                setInitialScale(80)
                settings.javaScriptEnabled = true
                webChromeClient = WebChromeClient()
                if(data!="")loadData(data,"text/html; charset=utf-8", "utf-8")
                else if(urlAddress!="")loadUrl(urlAddress)
            }
        }
    )
}

@Composable
fun WebView(data: MutableState<String>){
    AndroidView(
        modifier = Modifier.fillMaxHeight().fillMaxWidth(),
        factory = {
            WebView(activity).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                setInitialScale(80)
                settings.javaScriptEnabled = true
                webChromeClient = WebChromeClient()
                loadData(data.value,"text/html; charset=utf-8", "utf-8")
            }
        },
        update = {
            it.loadData(data.value,"text/html; charset=utf-8", "utf-8")
        }
    )
}
package com.fagougou.government.webViewPage

import android.util.Log
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
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Header
import com.fagougou.government.webViewPage.WebViewPageModel.data
import com.fagougou.government.webViewPage.WebViewPageModel.title
import com.fagougou.government.webViewPage.WebViewPageModel.urlAddress
import kotlinx.coroutines.*

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
            }
        },
        update = {
            it.loadDataWithBaseURL(null, data.value, "text/html; charset=utf-8", "utf-8", null)
            CoroutineScope(Dispatchers.Default).launch {
                delay(100)
                withContext(Dispatchers.Main){
                    it.evaluateJavascript("javascript:getHtml()",{ Log.i("html",it)})
                }
            }
        }
    )
}
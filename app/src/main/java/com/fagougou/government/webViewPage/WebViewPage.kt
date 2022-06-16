package com.fagougou.government.webViewPage

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.component.Header
import com.fagougou.government.webViewPage.WebViewPageModel.data
import com.fagougou.government.webViewPage.WebViewPageModel.title
import com.fagougou.government.webViewPage.WebViewPageModel.urlAddress

object WebViewPageModel{
    var title = ""
    var urlAddress = ""
    var data = ""

    fun clear(){
        title = ""
        urlAddress = ""
        data = ""
    }
}

@Composable
fun WebViewPage(navController: NavController) {
    Column {
        Header(title, navController, onBack = { WebViewPageModel.clear() })
        WebView(urlAddress,data)
    }
}

@Composable
fun WebView(urlAddress:String, data:String){
    AndroidView(
        {
            WebView(activity).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                setInitialScale(80)
                settings.javaScriptEnabled = true
                webChromeClient = WebChromeClient()
                if(data!="")loadData(data,"text/html; charset=utf-8", "utf-8")
                else if(urlAddress!="")loadUrl(urlAddress)
            }
        },
        Modifier.fillMaxHeight().fillMaxWidth()
    )
}

package com.fagougou.xiaoben.webViewPage

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
import com.fagougou.xiaoben.CommonApplication.Companion.activity
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.webViewPage.WebViewModel.data
import com.fagougou.xiaoben.webViewPage.WebViewModel.title
import com.fagougou.xiaoben.webViewPage.WebViewModel.urlAddress

object WebViewModel{
    var title = ""
    var urlAddress = ""
    var data = ""
}

@Composable
fun WebViewPage(navController: NavController) {
    Column {
        Headder(
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
                setInitialScale(100)
                settings.javaScriptEnabled = true
                webChromeClient = WebChromeClient()
                if(data!="")loadData(data,"text/html; charset=utf-8", "utf-8")
                else if(urlAddress!="")loadUrl(urlAddress)
            }
        }
    )
}
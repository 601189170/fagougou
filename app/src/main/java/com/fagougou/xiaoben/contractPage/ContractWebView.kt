package com.fagougou.xiaoben.contractPage

import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.xiaoben.CommonApplication.Companion.activityContext
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.contractPage.ContractWebView.codeBitmap
import com.fagougou.xiaoben.contractPage.ContractWebView.webViewUrl
import com.king.zxing.util.CodeUtils.createQRCode

object ContractWebView{
    var webViewUrl = ""
    var codeUrl = ""
    fun codeBitmap() = createQRCode(codeUrl.ifBlank { webViewUrl }, 256, null, Color.BLACK)
}

@Composable
fun ContractWebView(navController: NavController) {
    Row(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.75f),
            factory = {
                WebView(activityContext).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    setInitialScale(100)
                    settings.javaScriptEnabled = true
                    webChromeClient = WebChromeClient()
                    loadUrl(webViewUrl)
                }
            }
        )
        Surface(modifier = Modifier.fillMaxHeight().width(2.dp),color = ComposeColor(0xFFEEEEEE)){}
        Surface(color = ComposeColor.White){ Column(
            modifier = Modifier.fillMaxSize().padding(top = 96.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Image(bitmap = codeBitmap().asImageBitmap(), contentDescription = "QR Code of this page")
            Text("扫码保存到手机", fontSize = 24.sp)
        }}
    }
    Headder("合同文库", navController)
}
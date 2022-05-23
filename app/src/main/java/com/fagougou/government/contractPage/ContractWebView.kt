package com.fagougou.government.contractPage

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.component.Header
import com.fagougou.government.contractPage.ContractWebView.webViewUrl
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.qrCode.QrCodeViewModel

object ContractWebView{
    var webViewUrl = ""
    var codeUrl = ""
}

@Composable
fun ContractWebView(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth(),
            factory = {
                WebView(activity).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    setInitialScale(100)
                    settings.javaScriptEnabled = true
                    webChromeClient = WebChromeClient()
                    loadUrl(webViewUrl)
                }
            }
        )
        Surface(modifier = Modifier.fillMaxWidth().height(2.dp),color = ComposeColor(0xFFEEEEEE)){}
        Surface(color = ComposeColor.White){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(CORNER_FLOAT)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .width(200.dp),
                    onClick = {
                        QrCodeViewModel.show.value = true
                    },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_wechat),null)
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = "微信查看",
                                color = androidx.compose.ui.graphics.Color.White,
                                fontSize = 21.sp
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                )
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 24.dp)
                        .width(200.dp),
                    onClick = { },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_painter),null)
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = "打印模板",
                                color = Color.White,
                                fontSize = 21.sp)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Dodgerblue
                    )
                )
            }
        }
    }
    Header(
        "合同文库",
        navController,
        { QrCodeViewModel.currentUrl = QrCodeViewModel.constWechatUrl }
    )
}
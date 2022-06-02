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
import androidx.compose.material.ButtonDefaults.buttonColors
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
import com.fagougou.government.contractPage.ContractViewModel.officeUrl
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.utils.Printer.printWebView

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
                    setInitialScale(80)
                    settings.javaScriptEnabled = true
                    settings.setDefaultFontSize(40)
                    settings.setUseWideViewPort(true) //将图片调整到适合webview的大小
                    settings.setLoadWithOverviewMode(true) // 缩放至屏幕的大小
                    settings.setSupportZoom(false) //支持缩放，默认为true。是下面那个的前提。
                    settings.setBuiltInZoomControls(false) //设置内置的缩放控件。若为false，则该WebView不可缩放
                    settings.setDisplayZoomControls(true) //隐藏原生的缩放控件
                    settings.setAllowFileAccess(true)
                    webChromeClient = WebChromeClient()
                    loadUrl(officeUrl)
                }
            },
            update = {
                if (printWebView.value){
                    printWebView(it)
                    printWebView.value=false
                }
            }
        )
        Surface(modifier = Modifier
            .fillMaxWidth()
            .height(2.dp),color = ComposeColor(0xFFEEEEEE)){}
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
                        QrCodeViewModel.content.value = ContractViewModel.fileUrl
                        QrCodeViewModel.hint.value = "微信扫码下载"
                    },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_wechat),null)
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = "微信下载",
                                color = Color.White,
                                fontSize = 21.sp
                            )
                        }
                    },
                    colors = buttonColors(backgroundColor = Dodgerblue)
                )
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 24.dp)
                        .width(200.dp),
                    onClick = {
                        DialogViewModel.confirmPrint()
                    },
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
                    colors = buttonColors(backgroundColor = Dodgerblue)
                )
            }
        }
    }
    Header("合同文库",navController)
}
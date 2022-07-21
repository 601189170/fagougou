package com.fagougou.government.contractReviewPage

import android.view.ViewGroup
import android.webkit.WebSettings


import android.widget.LinearLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.component.Header


import com.tencent.smtt.sdk.WebView

@Composable
fun ResultWebviewPage(navController: NavController) {
//http://test.products.fagougou.com/usercenter/audit/ai-audit-main?type=private&id=62d778fcb4bc40254378e56b
    Surface(color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header("智能审查结果", navController)
            AndroidView(
                {
                    WebView(activity).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        settings.javaScriptEnabled = true
                        settings.javaScriptCanOpenWindowsAutomatically = true
                        settings.allowFileAccess = true
                        settings.setSupportZoom(true)
                        settings.builtInZoomControls = true
                        settings.useWideViewPort = true
                        settings.setSupportMultipleWindows(true)
                        // webSetting.setLoadWithOverviewMode(true);
                        settings.setAppCacheEnabled(true)
                        // webSetting.setDatabaseEnabled(true);
                        settings.domStorageEnabled = true
                        settings.setGeolocationEnabled(true)
                        settings.setAppCacheMaxSize(Long.MAX_VALUE)

                        settings.cacheMode = WebSettings.LOAD_NO_CACHE


//                            loadUrl(PreviewLoadModel.fileUrl)
                    }
                },
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.88f),
            ){


            }
        }
    }
}


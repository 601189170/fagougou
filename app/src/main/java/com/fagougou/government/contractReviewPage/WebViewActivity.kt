package com.fagougou.government.contractReviewPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.fagougou.government.databinding.ActivityWebviewBinding
import com.tencent.smtt.sdk.WebSettings

class WebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityWebviewBinding

    var url =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)

        setContentView(binding.root)
        url= intent.getStringExtra("data").toString()

        initWebViewSetting(binding.web.settings)
        binding.topLayout.tvZn.visibility= View.GONE
        binding.topLayout.tvWechat.visibility= View.GONE
        binding.topLayout.title.text="智能审核结果"
        binding.topLayout.tvBack.setOnClickListener(){
            finish()
        }
        Log.e("TAG", "onCreate==>: "+url )
    }

    fun initWebViewSetting(webSettings: WebSettings){
        webSettings.javaScriptEnabled = true
//        webSettings.javaScriptCanOpenWindowsAutomatically = true
//        webSettings.allowFileAccess = true
//        webSettings.setSupportZoom(true)
//        webSettings.builtInZoomControls = true
//        webSettings.useWideViewPort = true
//        webSettings.setSupportMultipleWindows(true)
//        // webSetting.setLoadWithOverviewMode(true);
//        webSettings.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSettings.domStorageEnabled = true
//        webSettings.setGeolocationEnabled(true)
//        webSettings.setAppCacheMaxSize(Long.MAX_VALUE)
//        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.web.loadUrl(url)

    }
}
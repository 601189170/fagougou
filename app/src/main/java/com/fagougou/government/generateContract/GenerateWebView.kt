package com.fagougou.government.generateContract

import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.blankj.utilcode.util.FileUtils
import com.fagougou.government.CommonApplication
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.generateContract.GenerateContractModel.readhtml
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.pop
import com.fagougou.government.utils.FileUtil.FilePath
import com.fagougou.government.utils.FileUtil.autoToHTML
import com.fagougou.government.utils.JS
import com.fagougou.government.utils.Printer
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException


@Composable
fun GenerateWebView() {
    AndroidView(
        {
            WebView(CommonApplication.activity).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setInitialScale(100)
                settings.javaScriptEnabled = true
                addJavascriptInterface( JS.getInstance(context),"local_obj");
                isVerticalScrollBarEnabled = false
                isClickable = false
                isLongClickable = false
                webChromeClient = WebChromeClient()
                webViewClient=object :WebViewClient(){
                        override fun onPageFinished(view: WebView?, url: String?) {
                            view?.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                            super.onPageFinished(view, url)
                        }

                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }



                CoroutineScope(Dispatchers.Default).launch {
                    while (true){
                        delay(1000)
                        withContext(Dispatchers.Main){
                            if (readhtml.value){
                                autoToHTML(FilePath, JS.getInstance(context).data)
                                HtmlToDoc(FileUtils.getFileByPath(FilePath))
                                readhtml.value=false
                            }

                        }
                    }

                }
            }
        },
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.88f),
        {
            it.loadDataWithBaseURL(
                null,
                GenerateContractViewModel.data.value,
                "text/html; charset=utf-8",
                "utf-8",
                null
            )
            if (Printer.webViewPrint.value) {
                Printer.printWebView(it)
                Printer.webViewPrint.value = false
            }
        }

    )
}



 fun HtmlToDoc(file:File){
     Client.globalLoading.value++
     var client =  OkHttpClient()

     val fileBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

     var requestBody = MultipartBody.Builder()
         .setType(MultipartBody.FORM)
         .addFormDataPart("file", file.name, fileBody)
         .build();

     var request = Request.Builder().url("https://products.fagougou.com/api/convert/from/html/to/docx")
         .post(requestBody)
         .build();

     client.newCall(request).enqueue(object : Callback {

         override fun onFailure(call: Call, e: IOException) {
             Client.globalLoading.pop()
             Log.e("TAG", "onFailure: "+e.toString() )

         }

         override fun onResponse(call: Call, response: Response) {
             Client.globalLoading.pop()
             if (response.code==200) {
                 var url = response.body?.string()
                 Log.e("TAG", "responsebody: "+url )
                 url?.let { QrCodeViewModel.set(it,"微信查看") }

             }


         }
     });

 }




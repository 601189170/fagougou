package com.fagougou.government.component.uploadGroup

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText

import com.fagougou.government.consult.TouristsLoginActivity
import com.fagougou.government.contractReviewPage.WebViewActivity

import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.generateContract.Html2DocCallback
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.model.ContractUrlBean
import com.fagougou.government.model.UpLoadBean2
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.pop
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.Printer
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips
import com.google.gson.Gson
import com.rajat.pdfviewer.PdfQuality
import com.rajat.pdfviewer.PdfRendererView
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.io.IOException



@Composable
fun PreviewLoad(
    subNavController: NavController,
    fullScreenMode: MutableState<Boolean>,
    routeTarget: String
) {
    Surface(color = Color.White) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!fullScreenMode.value) Text(
                "文档预览",
                Modifier.padding(top = 40.dp),
                fontSize = 28.sp,
                color = Color(0xFF303133)
            )
            Surface(
                Modifier
                    .height(if (fullScreenMode.value) 912.dp else 568.dp)
                    .fillMaxWidth()
                    .padding(if (fullScreenMode.value) 0.dp else 28.dp)
            ) {
                AndroidView(
                    {
                        PdfRendererView(activity).apply {
                            val url = Client.fileuploadUrl + UploadModel.taskId + ".pdf"
                            Timber.d(url)
                            initWithUrl(url, PdfQuality.NORMAL, "selfPrint")
                        }
                    },
                    Modifier.fillMaxSize()
                )
                Row(
                    Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .width(90.dp)
                        .height(35.dp)
                        .background(shape = RoundedCornerShape(10), color = Color(0xE2E0E0E6))) {
                        Image(painterResource(R.drawable.ic_icon_full_screen), null)
                        Text(
                            "全屏",
                            Modifier.clickable { fullScreenMode.value = !fullScreenMode.value },
                            fontSize = 20.sp,
                            color = Color(0xFF606366)
                        )
                    }

                }
            }
            Surface(
                Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = Color(0xFFDCE1E6)
            ) {}
            Row(
                Modifier.fillMaxSize(),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                Button(
                    {
                        with(DialogViewModel) {
                            clear()
                            title = "温馨提示"
                            firstButtonText.value = "取消"
                            firstButtonOnClick.value = { content.clear() }
                            secondButtonText.value = "确定"
                            secondButtonOnClick.value = {
                                content.clear()
                                fullScreenMode.value = false
                                subNavController.popBackStack(Router.Upload.waiting, true)
                            }
                            val note = when (routeTarget) {
                                Router.SelfPrint.printComplete -> "返回后将丢失本次上传的文件"
                                Router.ContractReview.result -> "返回后将丢失本次上传的文件"
                                else -> ""
                            }
                            content.add(ContentStyle(note))
                        }
                    },
                    Modifier
                        .height(64.dp)
                        .width(200.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    border = BorderStroke(2.dp, Dodgerblue),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 12.dp),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                ) { BasicText("返回上一级", color = Dodgerblue) }
                Spacer(modifier = Modifier.width(24.dp))
                Button(
                    {
                        fullScreenMode.value = false
                        when (routeTarget) {
                            Router.SelfPrint.printComplete -> DialogViewModel.confirmPrint(
                                File(
                                    activity.cacheDir,
                                    "selfPrint.pdf"
                                )
                            )
//                            Router.ContractReview.result -> Tips.toast("暂未开放功能")
                            Router.ContractReview.result -> getGenerateData(subNavController);
                        }
                    },
                    Modifier
                        .height(64.dp)
                        .width(200.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                    elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                ) {
                    val note = when (routeTarget) {
                        Router.SelfPrint.printComplete -> "开始打印"
                        Router.ContractReview.result -> "开始审核"
                        else -> ""
                    }
                    Text(note, Modifier, Color.White, 24.sp)
                }
            }
        }
    }
    Printer.isPrint.value = false
    LaunchedEffect(null) {
        if (routeTarget == Router.SelfPrint.printComplete) withContext(Dispatchers.Default) {
            while (isActive) {
                delay(250)
                Router.lastTouchTime = Time.stamp
                if (Printer.isPrint.value) {
                    withContext(Dispatchers.Main) {
                        subNavController.navigate(routeTarget)
                        Printer.isPrint.value = false
                    }
                }
            }
        }
    }
}


fun getGenerateData(subNavController: NavController){
    Client.globalLoading.value++
    val url = Client.tempUrl + UploadModel.taskId + ".docx"
    Timber.d("转换后doc地址==>"+url)
        val title="测试文件"
        val categoryId="234f2ed0-f1d6-11ec-ae28-bd7b6644f76f"
        val rulesType="系统推荐"
        val rulesTypeId="234f2ed0-f1d6-11ec-ae28-bd7b6644f76f"
        val ownerId="62d76f1bb4bc4065f578e409"
        val category="通用合同（新）"

        val okHttpClient = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("title", title)
            .addFormDataPart("categoryId", categoryId)
            .addFormDataPart("rulesType", rulesType)
            .addFormDataPart("rulesTypeId", rulesTypeId)
            .addFormDataPart("ownerId", ownerId)
            .addFormDataPart("category", category)
            .addFormDataPart("fileUrl", url).build();

        val request = Request.Builder().url("http://test.products.fagougou.com/api/contract-audit/out/upload").header("Authorization", "fagougou").post(requestBody).build()
        val call: Call = okHttpClient.newCall(request)

        call.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Client.globalLoading.pop()
            }
            override fun onResponse(call: Call, response: Response) {
                Client.globalLoading.pop()
                val body = response.body?.string()
                var bean = Gson().fromJson(body, ContractUrlBean::class.java)
                if (bean.code==0){
                    CoroutineScope(Dispatchers.IO).launch {
                        withContext(Dispatchers.Main) {
                            val intent = Intent(activity, WebViewActivity::class.java)
                            intent.putExtra("data",bean.data.url)
                            activity.startActivity(intent)
                            subNavController.popBackStack(Router.ContractReview.classify, false)
                        }
                    }
                }else{
                    Tips.toast("审查失败")
                }





            }
        })


}
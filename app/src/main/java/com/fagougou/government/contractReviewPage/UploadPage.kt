package com.fagougou.government.contractReviewPage

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bugsnag.android.Bugsnag
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.contractReviewPage.uploadModel.ispost
import com.fagougou.government.contractReviewPage.uploadModel.taskIdValue
import com.fagougou.government.contractReviewPage.uploadModel.uploadBitmap
import com.fagougou.government.repo.Client
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object uploadModel{
    var taskIdValue=""
    var ispost= mutableStateOf(false)
    val uploadBitmap = mutableStateOf( QrCodeViewModel.bitmap("null") )
}

@Composable
fun UploadPage(navController: NavController) {
    LaunchedEffect(null) {
        taskIdValue=""+Time.stamp+"_"+(0..999999).random()
        val url = "https://a.b/selfPrint?taskId=$taskIdValue"
        uploadBitmap.value=QrCodeViewModel.bitmap(url)
        while (!ispost.value){
            delay(1500)
            val request = Request.Builder().url(Client.fileuploadUrl+ taskIdValue +".pdf").get().build()
            Client.noLoadClient.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.code==200){
                        ispost.value=true
                        navController.navigate(Router.previewload)
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    Bugsnag.notify(e)
                }
            })
        }
    }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Top,
        Alignment.CenterHorizontally
    ) {
        Header("合同审查", navController,{QrCodeViewModel.clear()} )
        BasicText( "请使用文件上传方式",160.dp)
        BasicText("二选一即可", 100.dp,21.sp )
        Row(
            Modifier
                .padding(horizontal = 100.dp)
                .padding(top = 50.dp)
        ) {
            Box(
                Modifier
                    .clickable { navController.navigate(Router.scanupload) }
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(200.dp)){
                Image(uploadBitmap.value.asImageBitmap(),null)
            }
            Box(
                Modifier
                    .clickable {
                        val intent = Intent(activity, PaperUploadActivity::class.java)
                        activity.startActivity(intent)
                    }
                    .padding(start = 50.dp)
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(200.dp),
                contentAlignment=Alignment.Center
            ){
                BasicText("扫描纸质文件",0.dp,21.sp)
            }
        }
    }
}
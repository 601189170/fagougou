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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bugsnag.android.Bugsnag
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.contractReviewPage.UpLoadModel.ossUrl
import com.fagougou.government.contractReviewPage.UpLoadModel.taskId
import com.fagougou.government.contractReviewPage.UpLoadModel.uploadBitmap
import com.fagougou.government.repo.Client
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*
import okhttp3.Request
import timber.log.Timber


object UpLoadModel{
    const val ossUrl = "https://upload-1251511189.cos.ap-nanjing.myqcloud.com/"
    var taskId = ""
    val uploadBitmap = mutableStateOf( QrCodeViewModel.bitmap("null") )
}

@Composable
fun UploadPage(navController: NavController) {
    LaunchedEffect(null) {
        taskId=""+Time.stamp+"_"+(0..999999).random()
        val url = "$ossUrl$taskId.pdf"
        uploadBitmap.value=QrCodeViewModel.bitmap(url)
        withContext(Dispatchers.IO) {
            while (isActive) {
                delay(1500)
                Timber.d("Checking upload for ${taskId}.pdf")
                val request = Request.Builder().url(Client.fileuploadUrl+taskId +".pdf").get().build()
                val response = Client.noLoadClient.newCall(request).execute()
                if (response.code == 200) {
                    withContext(Dispatchers.Main){
                        navController.navigate(Router.previewLoad)
                    }
                }
            }
        }
    }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Top,
        Alignment.CenterHorizontally
    ) {
        BasicText( "请选择文件上传方式",160.dp)
        BasicText("二选一即可", 100.dp,21.sp )
        Row(
            Modifier
                .padding(horizontal = 100.dp)
                .padding(top = 50.dp)
        ) {
            Box(
                Modifier
                    .clickable { navController.navigate(Router.scanUpload) }
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(200.dp)){
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
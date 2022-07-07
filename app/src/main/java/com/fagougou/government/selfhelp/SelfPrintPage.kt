package com.fagougou.government.selfhelp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.repo.Client
import com.fagougou.government.contractReviewPage.UploadModel.generateSelfPrintUrl
import com.fagougou.government.contractReviewPage.UploadModel.taskId
import com.fagougou.government.utils.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okhttp3.Request
import timber.log.Timber

@Composable
fun SelfPrintPage(navController: NavController) {
    val uploadBitmap = remember{ mutableStateOf( QrCodeViewModel.bitmap("null") ) }
    LaunchedEffect(null) {
        taskId = "${Time.stamp}_"+(0..999999).random()
        val url = generateSelfPrintUrl(taskId,"selfPrint")
        uploadBitmap.value=QrCodeViewModel.bitmap(url,120)
        withContext(Dispatchers.IO){
            while (isActive){
                delay(1000)
                Timber.d("Checking upload for $taskId.tmp")
                val request = Request.Builder().url(Client.fileuploadUrl+taskId+".tmp").get().build()
                val response = Client.noLoadClient.newCall(request).execute()
                if (response.code == 200) {
                    withContext(Dispatchers.Main){
                        navController.navigate(Router.uploading)
                        QrCodeViewModel.clear()
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
        Text(
            "微信扫码打印",
            Modifier.padding( top = 32.dp),
            fontSize = 28.sp,
            color = Color(0xFF303133)
        )
        Text(
            "使用微信扫描以下二维码，上传文件成功后即可快速打印",
            Modifier.padding( top = 8.dp),
            fontSize = 20.sp,
            color = Color(0xFF303133)
        )
        Box(Modifier.padding(top = 44.dp),contentAlignment= Alignment.Center) {
            Image(painter = painterResource(id = R.drawable.img_print_code), contentDescription =null )
            Image(uploadBitmap.value.asImageBitmap(),null)
        }
    }
}
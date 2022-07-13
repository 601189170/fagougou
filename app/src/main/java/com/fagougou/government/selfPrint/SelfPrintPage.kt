package com.fagougou.government.selfPrint

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
import com.fagougou.government.component.uploadGroup.UploadModel.generateSelfPrintUrl
import com.fagougou.government.component.uploadGroup.UploadModel.taskId
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
                        navController.navigate(Router.Upload.waiting)
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
            Modifier.padding( top = 36.dp),
            fontSize = 28.sp,
            color = Color.Black
        )
        Text(
            "使用微信扫描以下二维码，上传文件成功后即可快速打印",
            Modifier.padding( top = 16.dp),
            fontSize = 20.sp,
            color = Color(0xFF909499)
        )
        Box(Modifier.padding(top = 48.dp),contentAlignment= Alignment.Center) {
            Image(painterResource(R.drawable.img_print_code), null, Modifier.padding(top = 16.dp) )
            Image(uploadBitmap.value.asImageBitmap(),"QR Code")
        }
    }
}
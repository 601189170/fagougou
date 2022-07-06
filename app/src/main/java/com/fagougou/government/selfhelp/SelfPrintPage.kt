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
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.model.StepModel
import com.fagougou.government.repo.Client
import com.fagougou.government.selfhelp.SelfPrintPageModel.generateSelfPrintUrl
import com.fagougou.government.selfhelp.SelfPrintPageModel.taskId
import com.fagougou.government.utils.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okhttp3.Request
import timber.log.Timber

object SelfPrintPageModel{
    var taskId = ""
    fun generateSelfPrintUrl(taskId:String):String{
        val selfPrintUrl = "https://www-1251511189.cos-website.ap-nanjing.myqcloud.com"
        return "$selfPrintUrl/?taskId=$taskId#/"
    }
}

@Composable
fun SelfPrintPage(navController: NavController) {
    val uploadBitmap = remember{ mutableStateOf( QrCodeViewModel.bitmap("null") ) }
    LaunchedEffect(null) {
        taskId = "${Time.stamp}_"+(0..999999).random()
        val url = generateSelfPrintUrl(taskId)
        uploadBitmap.value=QrCodeViewModel.bitmap(url)
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
            modifier = Modifier.padding( top = 32.dp),
            text = "微信扫码打印",
            fontSize = 28.sp,
            color = Color(0xFF303133)
        )
        Text(
            modifier = Modifier.padding( top = 8.dp),
            text = "使用微信扫描以下二维码，上传文件成功后即可快速打印",
            fontSize = 20.sp,
            color = Color(0xFF303133)
        )

            Box(Modifier.padding(top = 44.dp),contentAlignment= Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.img_print_code), contentDescription =null )
                Image(QrCodeViewModel.bitmap(generateSelfPrintUrl(taskId),120).asImageBitmap(),null)
            }




    }

}
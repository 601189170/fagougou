package com.fagougou.government.selfhelp

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
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
        val selfPrintUrl = "https://www-1251511189.cos-website.ap-nanjing.myqcloud.com/?taskId="
        return "$selfPrintUrl$taskId#/"
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState", )
@Composable
fun SelfPrintPage(navController: NavController) {
    val uploadBitmap = remember{ mutableStateOf( QrCodeViewModel.bitmap("null") ) }
    LaunchedEffect(null) {
        taskId = "${Time.stamp}_"+(0..999999).random()
        val url = generateSelfPrintUrl(taskId)
        uploadBitmap.value=QrCodeViewModel.bitmap(url)
        withContext(Dispatchers.IO){
            while (isActive){
                delay(1500)
                Timber.d("Checking upload for $taskId.pdf")
                val request = Request.Builder().url(Client.fileuploadUrl+taskId+".pdf").get().build()
                val response = Client.noLoadClient.newCall(request).execute()
                if (response.code == 200) {
                    withContext(Dispatchers.Main){
                        navController.navigate(Router.previewload)
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
        Header("自助打印", navController,{QrCodeViewModel.clear()} )
        BasicText( "请使用文件上传方式",160.dp)
        Row(
            Modifier
                .padding(horizontal = 100.dp)
                .padding(top = 50.dp)
        ) {
            Box(
                Modifier
                    .clickable { }
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(200.dp)){
                Image(uploadBitmap.value.asImageBitmap(),null)
            }
        }
    }
}
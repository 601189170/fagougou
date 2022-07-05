package com.fagougou.government.selfhelp

import android.annotation.SuppressLint
import android.util.Log
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
import com.fagougou.government.selfhelp.SelfPrintPageModel.taskIdValue
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object SelfPrintPageModel{

    var taskIdValue=""


}

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState", )
@Composable
fun SelfPrintPage(navController: NavController) {

    val uploadBitmap = remember{ mutableStateOf( QrCodeViewModel.bitmap("null") ) }
    LaunchedEffect( null ){
        taskIdValue =""+Time.stamp+"_"+(0..999999).random()

        val url = "https://www-1251511189.cos-website.ap-nanjing.myqcloud.com/?taskId="+taskIdValue+"#/"

        uploadBitmap.value=QrCodeViewModel.bitmap(url)
    }
    var ispost= mutableStateOf(false);
    LaunchedEffect(null) {
        while (!ispost.value){
            delay(1500)
            val request = Request.Builder().url(Client.fileuploadUrl+taskIdValue+".pdf").get().build()
             Client.noLoadClient.newCall(request).enqueue(object : Callback{
                override fun onResponse(call: Call, response: Response) {
                    if (response.code==200){
                        CoroutineScope(Dispatchers.IO).launch(Dispatchers.Main) {
                            withContext(Dispatchers.Main) {
                                ispost.value=true
                                navController.navigate(Router.previewload)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                }
            })
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
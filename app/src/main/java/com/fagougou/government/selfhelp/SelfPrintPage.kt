package com.fagougou.government.selfhelp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
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
        val selfPrintUrl = "https://www-1251511189.cos-website.ap-nanjing.myqcloud.com/?taskId="
        return "$selfPrintUrl$taskId#/"
    }
}

@Composable
fun SelfPrintPage(navController: NavController) {
    val uploadBitmap = remember{ mutableStateOf( QrCodeViewModel.bitmap("null") ) }
    val stepModel = remember{ StepModel(mutableStateListOf("文件上传","文档预览","完成打印"),mutableStateOf(0)) }

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
        Header("自助打印", navController,{QrCodeViewModel.clear()} )
        SelfHelpBase(stepModel) { }
    }
}
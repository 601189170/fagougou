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
import com.fagougou.government.CommonApplication
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.model.uploadBean
import com.fagougou.government.repo.Client
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*

object uploadModel{

    var navController2:NavController?=null

    init {
        CoroutineScope(Dispatchers.Default).launch(Dispatchers.Default) {
            while (true){
                delay(1500)
                val response = Client.serverlessService.uploadFile(CommonApplication.serial + "_" + System.currentTimeMillis() / 1000 + ".pdf").execute()
               if (response.code()==200){
                   val body = response.body() ?: uploadBean("")
                   withContext(Dispatchers.Main){
                       navController2?.navigate(Router.previewload)
                   }
               }

            }
        }
    }
}

@Composable
fun uploadPage(navController: NavController) {
    uploadModel.navController2 = navController
    val uploadBitmap = remember{ mutableStateOf( QrCodeViewModel.bitmap("null") ) }
    LaunchedEffect( null ){
        val url = "https://a.b/selfPrint?taskId="+Time.stamp+"_"+(0..999999).random()
        uploadBitmap.value=QrCodeViewModel.bitmap(url)
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
            Modifier.padding(horizontal = 100.dp).padding(top = 50.dp)
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
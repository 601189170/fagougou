package com.fagougou.government.selfhelp

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
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
import com.fagougou.government.chatPage.Case
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.model.CaseResponse
import com.fagougou.government.model.uploadBean
import com.fagougou.government.repo.Client
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*
import timber.log.Timber
object SelfPrintPageModel{


}

@Composable
fun SelfPrintPage(navController: NavController) {

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
        Header("自助打印", navController,{QrCodeViewModel.clear()} )
        BasicText( "请使用文件上传方式",160.dp)
        Row(
            Modifier.padding(horizontal = 100.dp).padding(top = 50.dp)
        ) {
            Box(
                Modifier
                    .clickable {  }
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(200.dp)){
                Image(uploadBitmap.value.asImageBitmap(),null)
            }
        }
    }
}
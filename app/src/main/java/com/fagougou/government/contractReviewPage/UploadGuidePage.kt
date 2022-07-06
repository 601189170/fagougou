package com.fagougou.government.contractReviewPage

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.consult.ChooseDomainActivity
import com.fagougou.government.consult.TouristsLoginActivity
import com.fagougou.government.contractReviewPage.UpLoadModel.generateSelfPrintUrl
import com.fagougou.government.contractReviewPage.UpLoadModel.ossUrl
import com.fagougou.government.contractReviewPage.UpLoadModel.selectId
import com.fagougou.government.contractReviewPage.UpLoadModel.taskId
import com.fagougou.government.contractReviewPage.UpLoadModel.uploadBitmap
import com.fagougou.government.repo.Client
import com.fagougou.government.selfhelp.SelfPrintPageModel
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips
import kotlinx.coroutines.*
import okhttp3.Request
import timber.log.Timber


object UpLoadModel{
    const val ossUrl = "https://upload-1251511189.cos.ap-nanjing.myqcloud.com/"
    var taskId = ""
    val uploadBitmap = mutableStateOf( QrCodeViewModel.bitmap("null") )
    var selectId= mutableStateOf(0)
    fun generateSelfPrintUrl(taskId:String):String{
        val Url = "https://www-1251511189.cos-website.ap-nanjing.myqcloud.com/?taskId="
        return "$Url$taskId#/"
    }
}

@Composable
fun UploadGuidePage(navController: NavController) {
    LaunchedEffect(null) {
        taskId=""+Time.stamp+"_"+(0..999999).random()
        val url = generateSelfPrintUrl(taskId)
        uploadBitmap.value=QrCodeViewModel.bitmap(url)
        withContext(Dispatchers.IO) {
            while (isActive) {
                delay(1500)
                Timber.d("Checking upload for ${taskId}.pdf")
                val request = Request.Builder().url(Client.fileuploadUrl+taskId +".pdf").get().build()
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
            modifier = Modifier.padding( top = 40.dp),
            text = "请选择文件上传方式",
            fontSize = 28.sp,
            color = Color(0xFF303133)
        )
        Row(
            Modifier
                .padding(horizontal = 100.dp)
                .padding(top = 28.dp)
        ) {
            Surface(
                Modifier
                    .clickable { selectId.value= 1 }
                    .width(416.dp)
                    .height(408.dp)
                    .border(1.dp, Color(if (selectId.value==1) 0xFF007BFF else 0xFFEBEDF0),RoundedCornerShape(8.dp )),
                shape = RoundedCornerShape(8.dp )) {
                Column(Modifier.padding(top = 32.dp,start = 64.dp,end = 64.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.img_code), contentDescription =null )
                    Text(
                        modifier = Modifier.padding(top = 28.dp),
                        text = "使用微信扫码上传",
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End) {
                    if (selectId.value==1)Image(painterResource(R.drawable.ic_icon_select_true), null)
                }

            }

            Surface(
                Modifier
                    .padding(start = 24.dp)
                    .clickable { selectId.value= 2}
                    .width(416.dp)
                    .height(408.dp)
                    .border(1.dp, Color(if (selectId.value==2) 0xFF007BFF else 0xFFEBEDF0),RoundedCornerShape(8.dp )),
                shape = RoundedCornerShape(8.dp )) {
                Column(Modifier.padding(top = 32.dp,start = 64.dp,end = 64.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.img_camera), contentDescription =null )
                    Text(
                        modifier = Modifier.padding(top = 28.dp),
                        text = "使用高拍仪扫描上传",
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End) {
                    if (selectId.value==2)Image(painterResource(R.drawable.ic_icon_select_true), null)
                }

            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            modifier = Modifier
                .height(64.dp)
                .width(200.dp),
            onClick = {
                when(selectId.value){
                    1 -> QrCodeViewModel.set(generateSelfPrintUrl(taskId),"微信扫码上传")
                    2 -> activity.startActivity(Intent(activity, PaperUploadActivity::class.java))
                    else -> { Tips.toast("请选择上传方式") }
                } },
            content = {
                Row( verticalAlignment = Alignment.CenterVertically ){
                    Text("立即上传",Modifier.padding(start = 16.dp),Color.White,21.sp)
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
        )

    }
}
package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.repo.Client
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.Printer
import com.fagougou.government.utils.Time
import com.rajat.pdfviewer.PdfQuality
import com.rajat.pdfviewer.PdfRendererView
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File

@Composable
fun PreviewLoad(navController2: NavController, navController: NavController, routeTarget:String) {
    Surface(color = Color.White){
        Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding( top = 40.dp),
                text = "文档预览",
                fontSize = 28.sp,
                color = Color(0xFF303133)
            )
            Surface(
                Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth()
                    .padding(28.dp)) {
                AndroidView(
                    {
                        PdfRendererView(activity).apply{
                            val url = Client.fileuploadUrl+ UploadModel.taskId +".pdf"
                            Timber.d(url)
                            initWithUrl(url, PdfQuality.NORMAL, "selfPrint")
                        }
                    },
                    Modifier.fillMaxSize()
                )
                Row(
                    Modifier.padding(16.dp),horizontalArrangement = Arrangement.End,verticalAlignment = Alignment.Bottom
                ) {
                    Image( painterResource(R.drawable.ic_icon_full_screen),null )
                    Text(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .clickable { },
                        text = "全屏",
                        fontSize = 20.sp,
                        color = Color(0xFF606366)
                    )
                }
            }
            Surface(modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),color = Color(0xFFDCE1E6)
            ){}
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    modifier = Modifier
                        .height(64.dp)
                        .width(200.dp),
                    content = { Text("返回上一级",Modifier,Dodgerblue,24.sp) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    border = BorderStroke(2.dp, Dodgerblue),
                    contentPadding = PaddingValues(horizontal = 36.dp,vertical = 12.dp),
                    elevation = ButtonDefaults.elevation(0.dp,0.dp),
                    onClick = {
                        with(DialogViewModel) {
                            clear()
                            title = "温馨提示"
                            firstButtonText.value = "取消"
                            firstButtonOnClick.value = { content.clear() }
                            secondButtonText.value = "确定"
                            secondButtonOnClick.value = {
                                content.clear()
                                navController2.navigate(routeTarget)
                            }
                            content.add( ContentStyle( "返回后将丢失本次上传的图片" ) )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(24.dp))
                Button(
                    modifier = Modifier
                        .height(64.dp)
                        .width(200.dp),
                    onClick = {
                        when (routeTarget) {
                            Router.printComplete -> DialogViewModel.confirmPrint(File(activity.cacheDir, "selfPrint.pdf"))
                            Router.resultWebview -> {
                                navController.navigate(Router.home)
                                navController.navigate(Router.resultWebview)
                            }
                        }
                    },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            val note = when(routeTarget){
                                Router.printComplete -> "开始打印"
                                Router.resultWebview -> "开始审核"
                                else -> ""
                            }
                            Text(note,Modifier,Color.White,24.sp)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                )
            }
        }
    }
    Printer.isPrint.value=false
    LaunchedEffect(null) {
        withContext(Dispatchers.IO) {
            while (isActive){
                delay(250)
                Router.lastTouchTime = Time.stamp
                if (Printer.isPrint.value){
                    withContext(Dispatchers.Main){
                        navController.navigate(Router.home)
                        navController.navigate(Router.printComplete)
                        Printer.isPrint.value=false
                    }
                }
            }
        }
    }
}
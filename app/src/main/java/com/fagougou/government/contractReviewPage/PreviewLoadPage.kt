package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.component.Header

import com.fagougou.government.repo.Client
import com.fagougou.government.selfhelp.SelfPrintPageModel

import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue

import com.fagougou.government.utils.SafeBack.safeBack
import com.rajat.pdfviewer.PdfQuality
import com.rajat.pdfviewer.PdfRendererView

object previewModel{


}
@Composable
fun Previewload(navController: NavController) {

    Surface(color = Color.White){
        Column(modifier = Modifier.fillMaxSize()) {
            Header("文档预览",navController)
            AndroidView(
                {
                    PdfRendererView(activity).apply{
                        initWithUrl(Client.fileuploadUrl+ SelfPrintPageModel.taskIdValue +".pdf", PdfQuality.NORMAL, "")
                    }
                },
                Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(),
            )
            Surface(modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),color = Color(0xFFEEEEEE)
            ){}
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(CORNER_FLOAT)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .width(200.dp),
                    onClick = {
                        navController.safeBack()
                    },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
//                            Image(painterResource(R.drawable.ic_wechat),null)
                            Text("返回",Modifier.padding(start = 16.dp),Color.White,21.sp)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                )
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 24.dp)
                        .width(200.dp),
                    onClick = { //调用审查接口
                        navController.navigate(Router.resultWebview) },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
//                            Image(painterResource(R.drawable.ic_painter),null)
                            Text("开始智能审核",Modifier.padding(start = 16.dp),Color.White,21.sp)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                )
            }
        }
    }
}
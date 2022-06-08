package com.fagougou.government.contractPage

import android.annotation.SuppressLint
import android.util.Xml
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.FileUtils.FILE_NAME
import com.fagougou.government.utils.FileUtils.FILE_PATH

import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import androidx.compose.ui.graphics.Color as ComposeColor

@SuppressLint("ResourceType")
@Composable
fun ContractWebView(navController: NavController) {
    Surface(color = ComposeColor.White){
        Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
            Header("合同文库",navController)
            var parser = activity.getResources().getXml(R.layout.textview);
            var attributes = Xml.asAttributeSet(parser);
            Row(Modifier.width(800.dp) .border(1.dp, Color.LightGray),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .fillMaxWidth(),
                    factory = {
                        PDFView(activity,attributes).apply {
                            fromFile(File(FILE_PATH+FILE_NAME)).load()
                        }
                    },
                    update = {
                    }
                )
            }

            Surface(modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),color = ComposeColor(0xFFEEEEEE)){}
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
                        QrCodeViewModel.content.value = ContractViewModel.fileUrl
                        QrCodeViewModel.hint.value = "微信扫码下载"
                    },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_wechat),null)
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = "微信下载",
                                color = Color.White,
                                fontSize = 21.sp
                            )
                        }
                    },
                    colors = buttonColors(backgroundColor = Dodgerblue)
                )
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(start = 24.dp)
                        .width(200.dp),
                    onClick = {
                        DialogViewModel.confirmPrint()
                    },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_painter),null)
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = "打印模板",
                                color = Color.White,
                                fontSize = 21.sp)
                        }
                    },
                    colors = buttonColors(backgroundColor = Dodgerblue)
                )
            }
        }
    }
}
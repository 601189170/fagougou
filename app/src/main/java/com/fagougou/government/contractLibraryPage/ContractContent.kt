package com.fagougou.government.contractLibraryPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
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
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.contractLibraryPage.ContractViewModel.BaseLoadUrl
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.CommonApplication.Companion.presentation
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.pop
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.MMKV.pdfKv
import com.rajat.pdfviewer.PdfQuality
import com.rajat.pdfviewer.PdfRendererView
import java.io.File
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun ContractContent(navController: NavController) {
    LaunchedEffect(null){
        presentation?.playVideo(R.raw.vh_contract_web_view)
    }
    Surface(color = ComposeColor.White){
        Column(modifier = Modifier.fillMaxSize()) {
            Header("合同文库",navController)
            AndroidView(
                {
                    PdfRendererView(activity).apply{
                        ContractViewModel.pdfFile?.let {
                            val hasPdfFile = File(activity.cacheDir, "${it.id}.pdf").exists()
                            val isNewest = pdfKv.decodeString(it.id) == it.updateAt
                            if(hasPdfFile && isNewest) initWithFile(File(activity.cacheDir, "${it.id}.pdf"))
                            else {
                                statusListener=object : PdfRendererView.StatusCallBack {
                                    override fun onDownloadStart() {
                                        Client.globalLoading.value++
                                        super.onDownloadStart()
                                    }
                                    override fun onDownloadSuccess(filePath: String) {
                                        Client.globalLoading.pop()
                                        super.onDownloadSuccess(filePath)
                                        pdfKv.encode(it.id, it.updateAt)
                                    }

                                    override fun onError(error: Throwable) {
                                        Client.globalLoading.pop()
                                        super.onError(error)
                                        Client.handleException(error)
                                    }
                                }
                                initWithUrl(BaseLoadUrl+ it.id, PdfQuality.NORMAL, it.id)
                            }
                        }
                    }
                },
                Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth(),
            )
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
                        ContractViewModel.pdfFile?.let {
                            QrCodeViewModel.set(BaseLoadUrl+ it.id,"微信扫码下载")
                        }
                    },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_wechat),null)
                            Text("微信下载",Modifier.padding(start = 16.dp),Color.White,21.sp)
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
                        DialogViewModel.confirmPrint(File(activity.cacheDir, "${ContractViewModel.pdfFile?.id ?: "0"}.pdf"))
                    },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_painter),null)
                            Text("打印模板",Modifier.padding(start = 16.dp),Color.White,21.sp)
                        }
                    },
                    colors = buttonColors(backgroundColor = Dodgerblue)
                )
            }
        }
    }
}
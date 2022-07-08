package com.fagougou.government.contractReviewPage

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.contractReviewPage.CamareModel.fileList
import com.fagougou.government.contractReviewPage.CamareModel.index
import com.fagougou.government.contractReviewPage.CamareModel.isPhoto
import com.fagougou.government.contractReviewPage.CamareModel.showFile
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.pop


import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.CameraUtils
import com.fagougou.government.utils.CameraUtils.ImgAddCallback
import com.fagougou.government.utils.MMKV
import com.fagougou.government.utils.Tips
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.rajat.pdfviewer.PdfRendererView
import java.io.File

object CamareModel{
    var isPhoto=mutableStateOf(true)

    var showFile=mutableStateOf(false)

    var fileList= mutableStateListOf<String>()

    var index=mutableStateOf(1)

}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun CameraPage(navController: NavController) {
    val pagerState = rememberPagerState()
    Box(Modifier.fillMaxSize()) {
        if (!showFile.value) {
            Column(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    {
                        PreviewView(activity).apply {
                            CameraUtils.initCamera(activity, this)

                            ImgAddCallback =object : ImageCapture.OnImageSavedCallback{
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    isPhoto.value=true
                                    CameraUtils.photoFile?.let { it1 -> fileList.add(it1.path) }
                                    Tips.toast("拍照成功")

                                }
                                override fun onError(exception: ImageCaptureException) {
                                    isPhoto.value=true
                                    Tips.toast("拍照失败")
                                }

                            }


                        }
                    },
                    modifier = Modifier
                        .height(836.dp)
                        .fillMaxWidth()
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(188.dp)
                        .background(Color.Black), contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (fileList.isNotEmpty())
                            Box(
                                Modifier
                                    .padding(end = 150.dp)
                                    .height(98.dp)
                                    .width(76.dp)) {
                                Image(
                                    rememberImagePainter(File(fileList[fileList.size - 1])),
                                    modifier = Modifier
                                        .rotate(-90f)
                                        .height(88.dp)
                                        .width(66.dp),
                                    contentDescription = null
                                )
                                Row(
                                    Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End) {

                                    Surface( modifier = Modifier
                                        .height(24.dp)
                                        .width(40.dp),color = Color.Blue, shape = RoundedCornerShape(12.dp),content = {
                                        Text("" + fileList.size, fontSize = 16.sp, color = Color.White,modifier = Modifier.padding(start = 15.dp))
                                    })

                                }

                            }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                if (isPhoto.value){
                                    isPhoto.value=false
                                    CameraUtils.takePhoto(activity)
                                }
                            }) {
                            Image(
                                painter = painterResource(id = R.drawable.img_camare),
                                contentDescription = null
                            )
                            Text(
                                "点击扫描",
                                Modifier.padding(top = 16.dp),
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                        if (fileList.isNotEmpty())
                            Text(
                            "完成扫描",
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 150.dp)
                                .clickable { showFile.value = true })
                    }


                }

            }
        }else{

            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE7E8E9)),horizontalAlignment =Alignment.CenterHorizontally) {
                Text(
                    "文档扫描列表",
                    Modifier.padding(top = 40.dp),
                    fontSize = 28.sp,
                    color = Color.Black
                )
                Text("当前预览("+index.value+"/"+fileList.size+"页）",
                    fontSize = 20.sp,
                    color = Color(0xFF606366),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { showFile.value = true })
                Row(Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center) {
                    Image(painter = painterResource(id = R.drawable.img_left), contentDescription = null,modifier = Modifier
                        .padding(start = 180.dp)
                        .clickable {
                            if (pagerState.currentPage != 0){
                                index.value=pagerState.currentPage - 1
                            }


                        })
                    HorizontalPager(
                        count = fileList.size,
                        state = pagerState,
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .width(550.dp)
                            .height(760.dp)
                    ) { index ->
                        if(index in 0 .. fileList.size-1){
                            PhotoImge(fileList[index])
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.img_right), contentDescription = null,modifier = Modifier
                        .padding(end = 180.dp)
                        .clickable {
                            if (pagerState.currentPage != fileList.size - 1){
                               index.value=pagerState.currentPage + 1

                            }
                        })

                }


                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .background(color = Color.White),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ){
                    Button(
                        modifier = Modifier
                            .height(60.dp)
                            .width(200.dp),
                        onClick = {
                            fileList.remove(fileList[pagerState.currentPage])
                            showFile.value=false
                        },
                        content = {
                            Row( verticalAlignment = Alignment.CenterVertically ){
                                Image(painterResource(R.drawable.ic_icon_re_photo),null)
                                Text("重拍本页",Modifier.padding(start = 4.dp), Dodgerblue,24.sp)
                            }
                        },
                        border = BorderStroke(2.dp, Dodgerblue),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        modifier = Modifier
                            .height(60.dp)
                            .width(200.dp),
                        onClick = { showFile.value=false },
                        content = {
                            Row( verticalAlignment = Alignment.CenterVertically ){
                                Image(painterResource(R.drawable.ic_icon_ct_photo),null)
                                Text("继续扫描",Modifier.padding(start = 4.dp), Dodgerblue,24.sp)
                            }
                        },
                        border = BorderStroke(2.dp, Dodgerblue),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        modifier = Modifier
                            .height(60.dp)
                            .width(200.dp),
                        onClick = {
                            //跳转到预览
                        },
                        content = {
                            Row( verticalAlignment = Alignment.CenterVertically ){
                                Text("下一步",Modifier,Color.White,24.sp)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                    )
                }


            }
        }

        Surface(Modifier.padding(start = 24.dp,top = 24.dp),color = Color(0x50000000),
            shape = RoundedCornerShape(32.dp)) {
            Row(verticalAlignment =Alignment.CenterVertically,horizontalArrangement = Arrangement.Center,modifier = Modifier.width(136.dp).height(64.dp).clickable {
                with(DialogViewModel) {
                    clear()
                    title = "温馨提示"
                    firstButtonText.value = "取消"
                    firstButtonOnClick.value = { content.clear() }
                    secondButtonText.value = "确定"
                    secondButtonOnClick.value = {
                        content.clear()
                        navController.popBackStack()
                    }
                    content.add( ContentStyle( "返回后将丢失本次上传的图片" ) )
                }
            }) {
                Image(painter = painterResource(id = R.drawable.ic_back), contentDescription =null )
                Text("返回", Modifier.padding(start = 5.dp), fontSize = 20.sp, color = Color.White)
            }
        }

    }
}


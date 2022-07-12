package com.fagougou.government.contractReviewPage.camera

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fagougou.government.CommonApplication
import com.fagougou.government.R
import com.fagougou.government.contractReviewPage.camera.CameraModel.fileList
import com.fagougou.government.contractReviewPage.camera.CameraModel.index
import com.fagougou.government.utils.CameraUtils
import com.fagougou.government.utils.Tips
import java.io.File

@Composable
fun PreviewPage(navController: NavController, appendMode:Boolean) {
    var isSingle by remember{ mutableStateOf(true) }
    Column {
        Box(Modifier.fillMaxWidth( )) {
            CameraPreview(
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        CameraUtils.photoFile?.let {
                            if (appendMode) fileList.add(it.path)
                            else if(index<fileList.size)fileList[index] = it.path
                            else fileList.add(it.path)
                        }
                        if (isSingle){
                            navController.popBackStack()
                        }
                        Tips.toast("拍照成功")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Tips.toast("拍照失败")
                    }
                }
            )
            if (appendMode) Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 750.dp)
                        .width(200.dp)
                        .height(50.dp),
                    Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "拍单张",
                            Modifier
                                .clickable { isSingle = true }
                                .background(if (isSingle) Color.White else Color.Black),
                            fontSize = 20.sp,
                            color = if (isSingle) Color.Black else Color.White
                        )
                        Text(
                            "拍多张",
                            Modifier
                                .clickable { isSingle = false }
                                .background(if (!isSingle) Color.White else Color.Black),
                            fontSize = 20.sp,
                            color = if (!isSingle) Color.Black else Color.White
                        )
                    }
                }
            BackButton(navController)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .height(188.dp)
                .background(Color.Black),
            Arrangement.Center,
            Alignment.CenterVertically,
        ) {
            if (fileList.isNotEmpty() && appendMode) Box(
                Modifier
                    .padding(end = 150.dp)
                    .height(98.dp)
                    .width(76.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            ) {
                fileList.lastOrNull()?.let {
                    Image(
                        rememberAsyncImagePainter(File(it)),
                        null,
                        Modifier
                            .rotate(-90f)
                            .height(88.dp)
                            .width(66.dp),
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.End
                    ) {
                        Surface(
                            Modifier
                                .height(24.dp)
                                .width(40.dp),
                            color = Color.Blue,
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(
                                fileList.size.toString(),
                                Modifier.padding(start = 15.dp),
                                fontSize = 16.sp,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
            Column(
                Modifier
                    .clickable { CameraUtils.takePhoto(CommonApplication.activity) },
                Arrangement.Top,
                Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(R.drawable.img_camare),
                    null
                )
                Text(
                    "点击扫描",
                    Modifier.padding(top = 16.dp),
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            if (fileList.isNotEmpty() && appendMode)
                Text(
                    "完成扫描",
                    Modifier
                        .padding(start = 150.dp)
                        .clickable {
                            navController.popBackStack()
                        },
                    fontSize = 20.sp,
                    color = Color.White,
                )
        }
    }
}
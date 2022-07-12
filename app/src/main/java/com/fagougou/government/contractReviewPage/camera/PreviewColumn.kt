package com.fagougou.government.contractReviewPage.camera

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.contractReviewPage.camera.CameraModel.index
import com.fagougou.government.ui.theme.Dodgerblue
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PreviewColumn(navController: NavController){
    val pagerState = rememberPagerState(CameraModel.fileList.size)
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        if(index==-1)index=0
        if(CameraModel.fileList.isEmpty())navController.navigate(Router.Scan.previewPage)
    }
    Box{
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFE7E8E9)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "文档扫描列表",
                Modifier.padding(top = 40.dp),
                fontSize = 28.sp,
                color = Color.Black
            )
            Text(
                "当前预览(" + (pagerState.currentPage+1) + "/" + CameraModel.fileList.size + "页）",
                Modifier
                    .padding(top = 8.dp),
                fontSize = 20.sp,
                color = Color(0xFF606366),
            )
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(800.dp), contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    CameraModel.fileList.size,
                    Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(),
                    pagerState,
                ) { index ->
                    if (index in 0 until CameraModel.fileList.size) {
                        Image(
                            rememberAsyncImagePainter(File(CameraModel.fileList[pagerState.currentPage])),
                            null,
                            Modifier.rotate(-90f),
                        )
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Image(
                        painterResource(if (pagerState.currentPage == 0) R.drawable.ic_banner_left_false else R.drawable.ic_banner_left_true),
                        null,
                        Modifier
                            .padding(start = 180.dp)
                            .clickable {
                                if (pagerState.currentPage > 0) {
                                    scope.launch {
                                        pagerState.scrollToPage(
                                            page = pagerState.currentPage - 1,
                                            pageOffset = 0f
                                        )
                                        index = pagerState.currentPage
                                        Timber.d("index$index")
                                    }
                                }
                            }
                    )
                    Image(
                        painterResource(
                            if (pagerState.currentPage == CameraModel.fileList.lastIndex) R.drawable.ic_banner_right_false
                            else R.drawable.ic_banner_right_true
                        ),
                        null,
                        Modifier
                            .padding(end = 180.dp)
                            .clickable {
                                if (pagerState.currentPage < CameraModel.fileList.lastIndex) {
                                    scope.launch {
                                        pagerState.scrollToPage(pagerState.currentPage + 1, 0f)
                                        index = pagerState.currentPage
                                        Timber.d("index$index")
                                    }
                                }
                            }
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .background(color = Color.White),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                Button(
                    { navController.navigate(Router.Scan.previewPageSingle) },
                    Modifier
                        .height(60.dp)
                        .width(200.dp),
                    border = BorderStroke(2.dp, Dodgerblue),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(R.drawable.ic_icon_re_photo), null)
                        Text("重拍本页", Modifier.padding(start = 4.dp), Dodgerblue, 24.sp)
                    }
                }
                Spacer(Modifier.width(20.dp))
                Button(
                    { navController.navigate(Router.Scan.previewPage) },
                    Modifier
                        .height(60.dp)
                        .width(200.dp),
                    border = BorderStroke(2.dp, Dodgerblue),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(R.drawable.ic_icon_ct_photo), null)
                        Text("继续扫描", Modifier.padding(start = 4.dp), Dodgerblue, 24.sp)
                    }
                }
                Spacer(Modifier.width(20.dp))
                Button(
                    {
                        //跳转到预览
                    },
                    Modifier
                        .height(60.dp)
                        .width(200.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("下一步", Modifier, Color.White, 24.sp)
                    }
                }
            }
        }
        BackButton(navController)
    }
}
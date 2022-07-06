package com.fagougou.government.selfhelp

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.Header
import com.fagougou.government.contractLibraryPage.ContractViewModel
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.pop
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.MMKV
import com.fagougou.government.utils.SafeBack.safeBack
import com.rajat.pdfviewer.PdfQuality
import com.rajat.pdfviewer.PdfRendererView
import java.io.File

@Composable
fun PrintCompletePage(navController: NavController) {
    Surface(color = Color.White) {
        Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
            Header("打印成功", navController)

            Image(modifier = Modifier.padding(top = 176.dp),painter = painterResource(id = R.drawable.img_phone_step2), contentDescription =null )
            Text(
                modifier = Modifier.padding( top = 32.dp),
                text = "打印成功",
                fontSize = 28.sp,
                color = Color(0xFF303133)
            )
            Text(
                modifier = Modifier.padding( top = 24.dp),
                text = "文件打印成功，请在下方打印机口取走相关文件，",
                fontSize = 28.sp,
                color = Color(0xFF303133)
            )

            Row(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){


                Button(
                    modifier = Modifier
                        .height(64.dp)
                        .width(200.dp),
                    onClick = { //调用审查接口
                        navController.navigate(Router.home)
                        navController.navigate(Router.self)
                         },
                    content = {
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Text("继续打印",Modifier,Color.White,24.sp)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Button(
                    modifier = Modifier
                        .height(64.dp)
                        .width(200.dp),
                    content = { Text("返回首页",Modifier,Dodgerblue,24.sp) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    border = BorderStroke(2.dp, Dodgerblue),
                    contentPadding = PaddingValues(horizontal = 36.dp,vertical = 12.dp),
                    elevation = ButtonDefaults.elevation(0.dp,0.dp),
                    onClick = {
                        navController.navigate(Router.home)
                    }

                )
            }
        }
    }
}
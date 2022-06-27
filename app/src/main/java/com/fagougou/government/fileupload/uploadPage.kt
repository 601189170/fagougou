package com.fagougou.government.fileupload

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.QrCodeViewModel

@Composable

fun uploadPage(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText( "请使用文件上传方式",160.dp)
        Text(
            modifier = Modifier
                .padding(top = 100.dp),
            text = "二选一即可",
            fontSize = 21.sp,
            color = Color.White
        )
        Row(

            Modifier
                .padding(horizontal = 100.dp).padding(top = 50.dp)
                ) {

            Box(
                Modifier
                    .clickable { navController.navigate(Router.scanupload) }
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(200.dp)){
                QrCodeViewModel.fileQRcode.value = CommonApplication.serial

                Image(QrCodeViewModel.FileUploadBitmap().asImageBitmap(),null)
            }

            Box(
                Modifier
                    .clickable {
                        val intent = Intent(activity, PaperUploadActivity::class.java)
                        activity.startActivity(intent) }
                    .padding(start=50.dp)
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(200.dp),
                contentAlignment=Alignment.Center){
                Text(
                    text = "扫描纸质文件",
                    fontSize = 21.sp,
                    color = Color.White
                )
            }
        }
    }
}
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.SafeBack.safeBack

@Composable

fun ScanUpload(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding( top = 40.dp),
            text = "请选择文件上传方式",
            fontSize = 28.sp,
            color = Color(0xFF303133)
        )
        Text(
            modifier = Modifier.padding( top = 8.dp),
            text = "请移至手机端进行操作，选择需要审查的文档并上传",
            fontSize = 20.sp,
            color = Color(0xFF909499)
        )
        Row(
            Modifier
                .clickable { navController.navigate(Router.previewLoad) }
                .padding(horizontal = 100.dp)
                .padding(top = 28.dp)
        ) {

            Image(painter = painterResource(id = R.drawable.img_phone_step1), contentDescription =null )
            Image(modifier = Modifier.padding(horizontal = 24.dp),painter = painterResource(id = R.drawable.img_phone_right), contentDescription =null )
            Image(painter = painterResource(id = R.drawable.img_phone_step2), contentDescription =null )
        }
        Text(
            modifier = Modifier.padding( top = 28.dp),
            text = "当前所在设备ID："+ CommonApplication.serial,
            fontSize = 24.sp,
            color = Color(0xFF303133)
        )
        Text(
            modifier = Modifier.padding( top = 8.dp),
            text = "注意：若离开此页面或者2分钟内未上传，将自动返回首页",
            fontSize = 20.sp,
            color = Color(0xFFFF423E)
        )
        Button(modifier = Modifier
            .padding(top = 36.dp)
                .height(64.dp)
                .width(200.dp),
            onClick = {
                navController.popBackStack() },
            content = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("返回上一级", Modifier.padding(start = 16.dp), Color.White, 21.sp)
                } },
            colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                )




    }
}
package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.SafeBack.safeBack

@Composable

fun Scanupload(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText("正在上传文档中 ，请勿离开该页面", 160.dp)
        Text(
            modifier = Modifier
                .padding(top = 100.dp),
            text = "请移至手机端进行操作，选择需要审查的文档并上传",
            fontSize = 21.sp,
            color = Color.White
        )
        Box(
            Modifier
                .clickable { navController.navigate(Router.previewload) }
                .padding(start = 50.dp)
                .width(500.dp)
                .height(500.dp),
            contentAlignment = Alignment.Center) {
            Text(
                text = "上传中...",
                fontSize = 36.sp,
                color = Color.White
            )
        }
        Column(
            Modifier.padding(top = 50.dp).height(75.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .width(200.dp),
                    onClick = {
                        navController.safeBack()
                    },
                    content = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Image(painterResource(R.drawable.ic_wechat),null)
                            Text("返回", Modifier.padding(start = 16.dp), Color.White, 21.sp)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                )
            }


        }
    }
}
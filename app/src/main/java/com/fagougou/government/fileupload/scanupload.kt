package com.fagougou.government.fileupload

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.ui.theme.Dodgerblue

@Composable

fun scanupload(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText( "正在上传文档中 ，请勿离开该页面",160.dp)
        Text(
            modifier = Modifier
                .padding(top = 100.dp),
            text = "请移至手机端进行操作，选择需要审查的文档并上传",
            fontSize = 21.sp,
            color = Color.White
        )




            Box(
                Modifier
                    .clickable {}
                    .padding(start=50.dp)
                    .background(Color.Gray)
                    .width(200.dp)
                    .height(200.dp),
                contentAlignment=Alignment.Center){
                Text(
                    text = "上传中...",
                    fontSize = 36.sp,
                    color = Color.White
                )
            }
        Button(
            onClick = { Router.lastTouchTime = 0L},
            content = { BasicText("返回", color = Dodgerblue) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            border = BorderStroke(2.dp, Dodgerblue),
            contentPadding = PaddingValues(horizontal = 36.dp,vertical = 12.dp),
            elevation = ButtonDefaults.elevation(0.dp,0.dp),
            shape = RoundedCornerShape(12)
        )

    }
}
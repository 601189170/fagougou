package com.fagougou.government.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R
import com.fagougou.government.model.StepModel

@Composable
fun ExBackground() {
    val stepModel = remember{ StepModel(mutableStateListOf("选择类型","文件上传","文档预览","查看报告"),mutableStateOf(1)) }
    Surface(
        Modifier.fillMaxSize(),
        color = Color(0xFFF5F7FA),
    ) {
        Column(Modifier.fillMaxWidth()) {
            Surface{
                Image(painterResource(R.drawable.examination_bg), null)
                Row(
                    Modifier.fillMaxWidth().padding(top = 40.dp),
                    Arrangement.Center
                ){
                    StepGraph(stepModel)
                }
            }
        }
    }
}

@Preview(widthDp = 1280, heightDp = 1024)
@Composable
fun PreviewGreeting() {
    ExBackground()
}
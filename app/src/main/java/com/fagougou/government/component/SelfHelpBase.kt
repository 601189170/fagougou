package com.fagougou.government.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fagougou.government.R
import com.fagougou.government.model.StepModel

@Composable
fun SelfHelpBase(stepModel:StepModel,content: @Composable () -> Unit) {
    Surface(
        Modifier.fillMaxSize(),
        color = Color(0xFFF5F7FA)
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
        Surface(
            Modifier
                .fillMaxSize()
                .padding(top = 168.dp, start = 24.dp, end = 24.dp),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            content()
        }
    }
}
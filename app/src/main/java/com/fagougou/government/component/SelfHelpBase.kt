package com.fagougou.government.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fagougou.government.R
import com.fagougou.government.model.StepModel

@Composable
fun SelfHelpBase(stepModel:StepModel, fullScreenMode: MutableState<Boolean>, content: @Composable () -> Unit) {
    Surface(
        Modifier.fillMaxSize(),
        color = Color(0xFFF5F7FA)
    ) {
        if (!fullScreenMode.value)Column(Modifier.fillMaxWidth()) {
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
                .padding(
                    top = if (fullScreenMode.value)0.dp else 168.dp,
                    start = if (fullScreenMode.value)0.dp else 24.dp,
                    end = if (fullScreenMode.value)0.dp else 24.dp
                ),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            content()
        }
    }
}
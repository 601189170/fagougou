package com.fagougou.government.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R
import com.fagougou.government.model.StepModel
import com.fagougou.government.ui.theme.Dodgerblue

@Composable
fun StepGraph(stepModel: StepModel) {
    Row(
        Modifier,
        Arrangement.Center,
        Alignment.Top
    ) {
        for((i,step) in stepModel.steps.withIndex()){
            val isOpened = i<=stepModel.currentIndex.value
            val isFinished = i<stepModel.currentIndex.value
            val baseAlpha = if(isOpened) 1f else 0.6f
            if(i!=0){
                Column {
                    Spacer(Modifier.height(22.dp))
                    Surface(Modifier.width(128.dp).height(4.dp).padding(horizontal = 13.dp),color = Color.White.copy(alpha = baseAlpha)) {}
                }
            }
            Column(
                Modifier,
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                Surface(
                    Modifier.width(54.dp).height(54.dp),
                    color = Color.Transparent
                ) {
                    Canvas(Modifier.fillMaxSize()) {
                        if(i==stepModel.currentIndex.value) drawCircle(
                            color = Color.White.copy(alpha = 0.5f),
                            center = Offset(x=size.width/2,y=size.height/2),
                            radius = size.width/2f
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = baseAlpha),
                            center = Offset(x=size.width/2,y=size.height/2),
                            radius = size.width/2.25f
                        )
                    }
                    Row(
                        Modifier.fillMaxSize(),
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        if(isFinished){
                            Image(painterResource(R.drawable.ic_tick),null)
                        }else{
                            Text(
                                (i+1).toString(),
                                color = Dodgerblue.copy(alpha = baseAlpha),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                BasicText(step,13.dp,21.sp,Color.White.copy(alpha = baseAlpha))
            }
        }
    }
}
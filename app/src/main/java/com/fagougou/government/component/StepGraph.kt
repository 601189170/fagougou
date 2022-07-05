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
            val isFinished = i<=stepModel.currentIndex.value
            val baseAlpha = if(isFinished) 1f else 0.6f
            if(i!=0){
                Column {
                    Spacer(Modifier.height(22.dp))
                    Surface(Modifier.width(128.dp).height(4.dp).padding(horizontal = 16.dp),color = Color.White.copy(alpha = baseAlpha)) {}
                }
            }
            Column(
                Modifier,
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                Surface(
                    Modifier
                        .width(48.dp)
                        .height(48.dp),
                    color = Color.Transparent
                ) {
                    Canvas(Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White.copy(alpha = baseAlpha),
                            center = Offset(x=size.width/2f,y=size.height/2),
                            radius = size.width/2
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
                                i.toString(),
                                color = Dodgerblue.copy(alpha = baseAlpha),
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                BasicText(step,16.dp,21.sp,Color.White.copy(alpha = baseAlpha))
            }
        }
    }
}
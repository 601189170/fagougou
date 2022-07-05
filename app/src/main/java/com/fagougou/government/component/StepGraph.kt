package com.fagougou.government.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fagougou.government.model.StepModel

@Composable
fun StepGraph(width:Int,stepModel: StepModel) {
    Row(
        Modifier.width(width.dp),
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically
    ) {
        for((i,step) in stepModel.steps.withIndex()){
            Surface() {
                Canvas(
                    Modifier
                        .width(38.dp)
                        .height(38.dp)) {
                    val canvasWidth = size.width  // 画布的宽
                    val canvasHeight = size.height  // 画布的高
                    drawOval(
                        color = Color.White,  // 颜色
                        // 偏移量
                        topLeft = Offset(canvasWidth / 2, canvasHeight / 2),
                        // 大小
                        size = Size(canvasWidth, canvasHeight)
                    )
                }
                Text(step)
            }
            if(i!=stepModel.steps.lastIndex){
                Canvas(Modifier.width(100.dp).height(3.dp)) {
                    val canvasWidth = size.width  // 画布的宽
                    val canvasHeight = size.height  // 画布的高
                    drawRect(
                        color = Color.White,  // 颜色
                        // 偏移量
                        topLeft = Offset(canvasWidth / 2, canvasHeight / 2),
                        // 大小
                        size = Size(canvasWidth, canvasHeight)
                    )
                }
            }
        }
    }
}
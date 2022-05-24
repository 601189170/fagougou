package com.fagougou.government.component


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BasicText(
    text:String,
    paddingTop: Dp = 0.dp,
    fontSize: TextUnit = 24.sp,
    color: Color = Color.White
) {
    Text(
        text,
        Modifier.padding(top = paddingTop),
        color = color,
        fontSize = fontSize
    )
}
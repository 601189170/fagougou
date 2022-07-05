package com.fagougou.government.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExContent() {
    Surface(
        Modifier
            .fillMaxSize()
            .padding(top = 168.dp, start = 24.dp, end = 24.dp),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp,topEnd = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .clickable { }
                .background(Color(0xFFECF5FF)),
            text = "内容",
            fontSize = 28.sp,
            color = Color(0xFF0E7AE6)
        )
    }
}


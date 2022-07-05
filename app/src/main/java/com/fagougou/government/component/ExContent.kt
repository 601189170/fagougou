package com.fagougou.government.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R
import com.fagougou.government.ui.theme.CORNER_FLOAT8

@Composable
fun ExContent() {

    Surface(
        Modifier
            .fillMaxSize()
            .padding(top = 168.dp, start = 24.dp, end = 24.dp)) {

        Box(Modifier.fillMaxWidth().background(color = Color.White).
        border(1.dp, Color(0xFFFFFFFF)
            , RoundedCornerShape(16f,16f)
        )) {
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
}


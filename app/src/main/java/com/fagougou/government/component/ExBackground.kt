package com.fagougou.government.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R

@Composable
fun ExBackground() {
    Surface(
        Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
        Box(Modifier.height(480.dp).fillMaxWidth()) {
        Image(painterResource(R.drawable.examination_bg), null)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                modifier = Modifier
                    .clickable { }
                    .background(Color(0xFFECF5FF)),

                text = "1",
                fontSize = 28.sp,
                color = Color(0xFF0E7AE6)
            )
            Text(
                modifier = Modifier
                    .clickable { }
                    .background(Color(0xFFECF5FF)),

                text = "2",
                fontSize = 28.sp,
                color = Color(0xFF0E7AE6)
            )
            Text(
                modifier = Modifier
                    .clickable { }
                    .background(Color(0xFFECF5FF)),

                text = "3",
                fontSize = 28.sp,
                color = Color(0xFF0E7AE6)
            )
            Text(
                modifier = Modifier
                    .clickable { }
                    .background(Color(0xFFECF5FF)),

                text = "4",
                fontSize = 28.sp,
                color = Color(0xFF0E7AE6)
            )
        }

        }
            Surface(Modifier.height(480.dp).background(Color(0xFFF5F7FA)).fillMaxWidth()) {

            }
        }
    }
}
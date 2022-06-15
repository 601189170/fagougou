package com.fagougou.government.chatPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.homePage.HomeButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ceshi (){
    Surface(Modifier.fillMaxSize()) {
        

    Column(
        modifier = Modifier
            .verticalScroll(ScrollState(0))
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        LazyHorizontalGrid( modifier = Modifier.height(48.dp),rows = GridCells.Fixed(1),userScrollEnabled = false,

            content = {
                items(3) { bot ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(15.dp)
                            .height(48.dp)) {
                        Text(
                            modifier = Modifier
                                .background(Color(0xFFECF5FF))
                                .padding(20.dp)
                                .fillMaxSize(),
                            text = "33333",
                            fontSize = 20.sp,
                            color = Color(0xFF0E7AE6)
                        )
                    }
                }
            })



        Surface(
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(1.dp), color = Color.Gray
        ) {

        }
    }

    }
}
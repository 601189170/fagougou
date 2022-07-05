package com.fagougou.government.contractReviewPage


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.Router

import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel


@Composable
fun ContractSelectPage(navController: NavController) {


    Column(
        Modifier.fillMaxSize(),
        Arrangement.Top,
        Alignment.CenterHorizontally
    ) {
        Header("合同类型", navController,{QrCodeViewModel.clear()} )
        LazyHorizontalGrid( rows = GridCells.Fixed(1),contentPadding=PaddingValues(20.dp),userScrollEnabled = false,
            content = {
                items(6) { bot ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            modifier = Modifier
                                .clickable { navController.navigate(Router.upload) }
                                .background(Color(0xFFECF5FF))
                                .padding(horizontal = 20.dp,vertical = 10.dp),
                            text = "劳动合同",
                            fontSize = 20.sp,
                            color = Color(0xFF0E7AE6)
                        )
                    }
                }
            })
    }
}
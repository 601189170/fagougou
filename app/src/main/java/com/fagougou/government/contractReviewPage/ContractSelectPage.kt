package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.Router

@Composable
fun ContractSelectPage(navController: NavController) {
    Column {
        Text(
            "请选择合同类型",
            Modifier.padding( top = 40.dp),
            Color(0xFF303133),
            28.sp
        )
        LazyVerticalGrid(
            GridCells.Fixed(5),
            userScrollEnabled = false,
            contentPadding=PaddingValues(30.dp),
            modifier = Modifier
                .padding(horizontal = 56.dp)
                .padding(top = 24.dp),
            content = {
                items(10){
                    Column(
                        Modifier
                            .clickable { navController.navigate(Router.upload) }
                            .height(240.dp)
                            .width(208.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column( Modifier.background(Color.White),horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                Modifier
                                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                    .width(185.dp)
                                    .height(156.dp)
                                    .background(Color(0xFFEFF5FC))) {
                            }
                            Text(
                                modifier = Modifier.padding(top = 15.dp,),
                                text = "通用合同",
                                fontSize = 20.sp,
                                color = Color(0xFF303133)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }
                }
            }
        )
    }
}
package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.VerticalGrid

@Composable
fun ContractSelectPage(navController: NavController) {
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        BasicText("请选择合同类型",40.dp,21.sp,Color(0xFF303133))
        Spacer(Modifier.height(28.dp))
        VerticalGrid(
            dataList = listOf("通用合同","通用合同","通用合同","通用合同","通用合同","通用合同","通用合同","通用合同",),
            columnNumber = 5,
            20.dp,
            240.dp,
            208.dp
        ) {  modifier,it->
            Card(
                modifier
                    .shadow(6.dp)
                    .clickable { navController.navigate(Router.upload) },
            ){
                Column(
                    Modifier,
                    Arrangement.Top,
                    Alignment.CenterHorizontally
                ) {
                    Surface(
                        Modifier
                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                            .width(192.dp)
                            .height(156.dp),
                        color = Color(0xFFEFF5FC)
                    ) { }
                    BasicText(it,20.dp,24.sp,Color(0xFF303133))
                }
            }
        }
    }
}
package com.fagougou.government.contractLibraryPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.fagougou.government.model.ContractData

@Composable
fun ContractLibraryItem(navController: NavController, category: ContractData){
    Column(
        Modifier.clickable {
            ContractViewModel.getTemplate(category, navController)
        }
    ){
        Row(
            Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(painterResource(R.drawable.ic_word), null)
            Text(
                category.name,
                Modifier.padding(start = 8.dp),
                fontWeight= FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
        }
        Text(
            category.howToUse ?: "暂无数据",
            Modifier.padding(top = 12.dp),
            fontSize = 20.sp,
            lineHeight = 36.sp,
            color = Color.Black
        )
        Text(
            "行业类型：" + (category.folder?.name ?: "暂无数据"),
            Modifier.padding(top = 12.dp,bottom = 16.dp),
            fontSize = 18.sp,
            color = Color.Gray
        )
        Surface(
            Modifier
                .fillMaxWidth()
                .height(1.dp),color = Color.LightGray
        ){}
    }
}
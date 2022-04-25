package com.fagougou.xiaoben.statisticPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.model.About
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.repo.Client.apiService
import com.fagougou.xiaoben.statisticPage.Statistic.allNumber
import com.fagougou.xiaoben.statisticPage.Statistic.monthNumber

object Statistic{
    val allNumber = mutableStateOf(0)
    val monthNumber = mutableStateOf(0)
    val qrcodeUrl = mutableStateOf("")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.aboutRobot().execute()
            val body = response.body() ?: About()
            allNumber.value = body.aboutData.aboutFirm.allNumber
            monthNumber.value = body.aboutData.aboutFirm.monthNumber
            qrcodeUrl.value = body.aboutData.aboutFirm.qrcodeUrl
        }
    }
}

@Composable
fun StatisticPage(navController:NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Headder("咨询数据",navController)
        Text(
            modifier = Modifier.padding(top = 96.dp),
            text = "咨询数据统计",
            fontSize = 45.sp,
            color = Color.White
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "为您统计每月和历史所有咨询数据",
            fontSize = 28.sp,
            color = Color.White
        )
        Row(
            modifier = Modifier.padding(top = 64.dp),
        ) {
            Surface(
                modifier = Modifier
                    .padding(end = 18.dp)
                    .width(472.dp)
                    .height(240.dp),
                color = Color.Transparent
            ){
                Image(
                    painter = painterResource(R.drawable.statistic_month),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.padding(34.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "本月咨询人数",
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    Text(
                        text = monthNumber.value.toString(),
                        fontSize = 38.sp,
                        color = Color.White
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .width(472.dp)
                    .height(240.dp),
                color = Color.Transparent
            ){
                Image(
                    painter = painterResource(R.drawable.statistic_history),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.padding(34.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "历史咨询人数",
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    Text(
                        text = allNumber.value.toString(),
                        fontSize = 38.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
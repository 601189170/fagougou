package com.fagougou.government.statisticPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.component.Header
import com.fagougou.government.model.About
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.fagougou.government.R
import com.fagougou.government.component.BasicText
import com.fagougou.government.repo.Client.apiService
import com.fagougou.government.statisticPage.Statistic.allNumber
import com.fagougou.government.statisticPage.Statistic.monthNumber

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
        Header("咨询数据",navController)
        BasicText("咨询数据统计",96.dp,32.sp)
        BasicText("为您统计每月和历史所有咨询数据",8.dp)
        Row(
            modifier = Modifier.padding(top = 40.dp),
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
                    BasicText("本月咨询人数",0.dp,28.sp)
                    BasicText(monthNumber.value.toString(),0.dp,48.sp)
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
                    BasicText("历史咨询人数",0.dp,28.sp)
                    BasicText(allNumber.value.toString(),0.dp,48.sp)
                }
            }
        }
    }
}
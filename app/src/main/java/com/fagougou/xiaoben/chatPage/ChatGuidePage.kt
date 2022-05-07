package com.fagougou.xiaoben.chatPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.chatPage.ChatViewModel.selectedChatBot
import com.fagougou.xiaoben.chatPage.ChatViewModel.startChat
import com.fagougou.xiaoben.homePage.HomeButton
import com.fagougou.xiaoben.utils.IFly
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ChatGuidePage(navController: NavController) {
    val scope = rememberCoroutineScope()
    val botResMap = mapOf(
        Pair("婚姻家事", R.drawable.bot_marry),
        Pair("员工纠纷", R.drawable.bot_employee),
        Pair("交通事故", R.drawable.bot_traffic),
        Pair("企业人事", R.drawable.bot_employer),
        Pair("民间借贷", R.drawable.bot_loan),
        Pair("公司财税", R.drawable.bot_tax),
        Pair("房产纠纷", R.drawable.bot_house),
        Pair("知识产权", R.drawable.bot_knowledge),
        Pair("刑事犯罪", R.drawable.bot_crime),
        Pair("消费维权", R.drawable.bot_consumer),
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Headder(title = "智能咨询", navController = navController,needWechat = true)
        Text(
            modifier = Modifier.padding(top = 96.dp),
            text = "智能咨询",
            fontSize = 45.sp,
            color = Color.White
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "秒问秒答，快速解答法律疑惑",
            fontSize = 28.sp,
            color = Color.White
        )
        LazyVerticalGrid(
            userScrollEnabled = false,
            modifier = Modifier.padding(64.dp),
            columns = GridCells.Fixed(4),
            content = {
                items(botResMap.toList()){ bot ->
                    Column(modifier= Modifier
                        .fillMaxSize()
                        .height(292.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                        HomeButton(
                            modifier= Modifier
                                .width(218.dp)
                                .height(272.dp),
                            onClick = {
                                IFly.isEnable = true
                                selectedChatBot.value = bot.first
                                scope.launch(Dispatchers.IO) { startChat() }
                                navController.navigate("chat")
                            },
                            contentId = bot.second
                        )
                    }
                }
            }
        )
    }
}
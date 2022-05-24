package com.fagougou.government.chatPage

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
import com.fagougou.government.R
import com.fagougou.government.component.Header
import com.fagougou.government.chatPage.ChatViewModel.selectedChatBot
import com.fagougou.government.chatPage.ChatViewModel.startChat
import com.fagougou.government.homePage.HomeButton
import com.fagougou.government.component.QrCodeViewModel.constWechatUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ChatGuidePage(navController: NavController) {
    val scope = rememberCoroutineScope()
    val botResMap = mapOf(
        Pair("公司财税", R.drawable.bot_tax),
        Pair("交通事故", R.drawable.bot_traffic),
        Pair("婚姻家事", R.drawable.bot_marry),
        Pair("员工纠纷", R.drawable.bot_employee),
        Pair("知识产权", R.drawable.bot_knowledge),
        Pair("刑事犯罪", R.drawable.bot_crime),
        Pair("房产纠纷", R.drawable.bot_house),
        Pair("企业人事", R.drawable.bot_employer),
        Pair("民间借贷", R.drawable.bot_loan),
        Pair("消费维权", R.drawable.bot_consumer),
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            "智能咨询",
            navController,
            qrCode = constWechatUrl)
        Text(
            modifier = Modifier.padding(top = 96.dp),
            text = "智能咨询",
            fontSize = 32.sp,
            color = Color.White
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "秒问秒答，快速解答法律疑惑",
            fontSize = 24.sp,
            color = Color.White
        )
        LazyVerticalGrid(
            userScrollEnabled = false,
            modifier = Modifier.padding(top = 40.dp ).padding(horizontal = 100.dp),
            columns = GridCells.Fixed(5),
            content = {
                items(botResMap.toList()){ bot ->
                    Column(modifier= Modifier
                        .fillMaxSize()
                        .height(250.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                        HomeButton(
                            modifier= Modifier
                                .width(184.dp)
                                .height(224.dp),
                            onClick = {
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
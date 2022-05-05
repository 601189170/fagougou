package com.fagougou.xiaoben.chatPage

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.CommonApplication.Companion.TAG
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.chatPage.ChatViewModel.botQueryIdMap
import com.fagougou.xiaoben.chatPage.ChatViewModel.chatIoState
import com.fagougou.xiaoben.chatPage.ChatViewModel.currentProvince
import com.fagougou.xiaoben.chatPage.ChatViewModel.getComplex
import com.fagougou.xiaoben.chatPage.ChatViewModel.history
import com.fagougou.xiaoben.chatPage.ChatViewModel.listState
import com.fagougou.xiaoben.chatPage.ChatViewModel.needAddressNow
import com.fagougou.xiaoben.chatPage.ChatViewModel.nextChat
import com.fagougou.xiaoben.chatPage.ChatViewModel.selectedChatBot
import com.fagougou.xiaoben.chatPage.ChatViewModel.startChat
import com.fagougou.xiaoben.homePage.HomeButton
import com.fagougou.xiaoben.model.CityMap
import com.fagougou.xiaoben.model.Message
import com.fagougou.xiaoben.model.Speaker
import com.fagougou.xiaoben.ui.theme.CORNER_FLOAT
import com.fagougou.xiaoben.ui.theme.Dodgerblue
import com.fagougou.xiaoben.utils.IFly
import com.fagougou.xiaoben.utils.TTS.mTts
import kotlinx.coroutines.CoroutineScope

@Composable
fun BotMenu() {
    val botResMap = mapOf(
        Pair("婚姻家事", R.drawable.bot_small_marry),
        Pair("员工纠纷", R.drawable.bot_small_employee),
        Pair("交通事故", R.drawable.bot_small_traffic),
        Pair("企业人事", R.drawable.bot_small_employer),
        Pair("民间借贷", R.drawable.bot_small_loan),
        Pair("公司财税", R.drawable.bot_small_tax),
        Pair("房产纠纷", R.drawable.bot_small_house),
        Pair("知识产权", R.drawable.bot_small_knowledge),
        Pair("刑事犯罪", R.drawable.bot_small_crime),
        Pair("消费维权", R.drawable.bot_small_consumer),
    )
    val botList = botQueryIdMap.toList()
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .padding(16.dp)
    ) {
        for (bot in botList) Column(modifier = Modifier.padding(8.dp)) {
            HomeButton(
                modifier = Modifier
                    .width(136.dp)
                    .height(171.dp),
                onClick = {
                    selectedChatBot.value = bot.first
                    startChat()
                },
                contentId = botResMap[bot.first] ?: R.drawable.bot_small_unknow
            )
        }
    }
}

@Composable
fun LawExpend(message: Message,index: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (message.isExpend) for ((i, law) in message.laws.withIndex()) {
            Text(
                (i + 1).toString() + "." + law.name + law.position + ":",
                modifier = Modifier.padding(16.dp),
                fontSize = 25.sp
            )
            Text(
                law.content,
                modifier = Modifier.padding(12.dp),
                color = Color(0xFF666666),
                fontSize = 22.sp
            )
        } else Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .clickable { history[index] = message.copy(isExpend = true) }
        ) {
            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "点击查看法律依据",
                fontSize = 24.sp,
                color = Dodgerblue
            )
            Image( painterResource(R.drawable.ic_expend), null )
        }
    }
}

@Composable
fun MessageRect(
    message: Message,
    index:Int,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
) {
    Surface(
        shape = RoundedCornerShape(CORNER_FLOAT),
        color = backgroundColor,
    ) {
        Column{
            Text(
                modifier = Modifier.padding(16.dp),
                text = message.content+message.complex.explanation,
                fontSize = 28.sp,
                color = textColor,
            )
            if (message.laws.isNotEmpty()) Divider(
                color = Color(0xFFCCCCCC),
                thickness = 2.dp,
            )
            if (message.laws.isNotEmpty()) LawExpend(message,index)
        }
    }
}

@Composable
fun ComplexRect(
    message: Message,
    index:Int,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    navController: NavController
) {
    Surface(
        shape = RoundedCornerShape(CORNER_FLOAT),
        color = backgroundColor,
    ) {
        Column{
            Surface( color = Dodgerblue ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        message.complex.title,
                        fontSize = 28.sp,
                        color = Color.White,
                    )
                }
            }
            Text(
                modifier = Modifier.padding(16.dp),
                text = message.content+message.complex.explanation,
                fontSize = 28.sp,
                color = textColor,
            )
            Divider(
                color = Color(0xFFCCCCCC),
                thickness = 2.dp
            )
            if (message.laws.isNotEmpty()) LawExpend(message,index)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { getComplex(message.complex.attachmentId, navController) },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text="点击查看更多",
                    fontSize = 24.sp,)
                Image( painterResource(R.drawable.ic_expend), null )
            }
        }
    }
}

@Composable
fun MessageItem(message: Message,index:Int, scope: CoroutineScope, listState: LazyListState,navController: NavController) {
    when (message.speaker) {
        Speaker.ROBOT -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) { MessageRect(message,index) }
        Speaker.USER -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) { MessageRect(message,index, Dodgerblue, Color.White) }
        Speaker.RECOMMEND -> Column(
            modifier = Modifier
                .padding(vertical = 18.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(CORNER_FLOAT),
                color = Color.White,
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    if (message.isExpend){
                        Row(
                            modifier = Modifier.padding(start = 16.dp,bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            if (index == 1) Text("大家都在问:", fontSize = 28.sp)
                            else {
                                Image(painterResource(R.drawable.ic_relate_question),null)
                                Text(modifier = Modifier.padding(start = 16.dp),text = "相关问题", fontSize = 28.sp)
                            }
                        }
                        for (question in message.recommends) Button(
                            onClick = { nextChat(question) },
                            content = { Text("·$question", fontSize = 28.sp, color = Dodgerblue) },
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            elevation = ButtonDefaults.elevation(0.dp)
                        )
                    } else Row(
                        modifier = Modifier
                            .clickable { history[index] = message.copy(isExpend = true) }
                    ) {
                        Image(painterResource(R.drawable.ic_relate_question), null)
                        Text(
                            modifier = Modifier.padding(start = 24.dp),
                            text = "点击查看与您情况相关的问题",
                            fontSize = 28.sp,
                            color = Dodgerblue
                        )
                    }
                }
            }
        }
        Speaker.OPTIONS -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
        ) {
            when (message.option.type) {
                "radio" -> {
                    val lastIndex = message.option.items.lastIndex
                    for (y in 0..lastIndex step 4) Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (x in 0..3){
                            val i = y+x
                            if(i<=lastIndex){
                                val item = message.option.items[i]
                                Button(
                                    modifier = Modifier.height(80.dp).width(225.dp),
                                    onClick = { nextChat(item) },
                                    content = {
                                        Text(
                                            text = item,
                                            fontSize = 24.sp,
                                            color = Color.White
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(Dodgerblue)
                                )
                            }
                        }
                    }
                }
                "address-with-search" -> {
                    needAddressNow.value = true
                }
            }
        }
        Speaker.COMPLEX -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) { ComplexRect(message,index, navController = navController) }
    }
}

@Composable
fun ChatPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Headder(
            "智能咨询(${selectedChatBot.value})",
            navController,
            onBack = {
                IFly.isEnable = false
                IFly.wakeMode()
                mTts.stopSpeaking()
                history.clear()
            }
        )
        BotMenu()
        val scope = rememberCoroutineScope()
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .padding(horizontal = 36.dp),
            verticalArrangement = Arrangement.Top,
            state = listState,
        ) {
            items(history.size) { index -> MessageItem(history[index],index, scope, listState, navController) }
            if(chatIoState.value) item { MessageItem(Message(Speaker.ROBOT, content = ". . ."),-1, scope, listState, navController) }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ){
                    if(needAddressNow.value){
                        if(currentProvince.value == "") for (province in CityMap.provinceList.keys)
                            Button(
                                onClick = {
                                    currentProvince.value = province
                                },
                                content = {Text(province)}
                            )
                        else for(city in CityMap.provinceList[currentProvince.value] ?: listOf())
                            Button(
                                onClick = {
                                    nextChat(currentProvince.value+"-$city")
                                    currentProvince.value = ""
                                    needAddressNow.value = false
                                },
                                content = {Text(city)}
                            )
                    }
                }
            }
            item{
                Text(
                    modifier = Modifier.clickable { nextChat("预测起诉离婚的成功率和查看相关案例") },
                    text = "预测离婚"
                )
            }
            item{
                Text(
                    modifier = Modifier.clickable { nextChat("北京市") },
                    text = "北京市"
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = 32.dp),
                text = IFly.recognizeResult.value,
                fontSize = 32.sp,
                color = Color.White)
            PAG()
        }
    }
    BackHandler(enabled = true) {
        Log.d(TAG,"Back")
    }
}
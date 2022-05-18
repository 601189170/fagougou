package com.fagougou.government.chatPage

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.Header
import com.fagougou.government.R
import com.fagougou.government.chatPage.ChatViewModel.botQueryIdMap
import com.fagougou.government.chatPage.ChatViewModel.currentProvince
import com.fagougou.government.chatPage.ChatViewModel.getComplex
import com.fagougou.government.chatPage.ChatViewModel.history
import com.fagougou.government.chatPage.ChatViewModel.listState
import com.fagougou.government.chatPage.ChatViewModel.nextChat
import com.fagougou.government.chatPage.ChatViewModel.selectedChatBot
import com.fagougou.government.chatPage.ChatViewModel.showBotMenu
import com.fagougou.government.chatPage.ChatViewModel.startChat
import com.fagougou.government.chatPage.ChatViewModel.textInputContent
import com.fagougou.government.chatPage.ChatViewModel.voiceInputMode
import com.fagougou.government.homePage.HomeButton
import com.fagougou.government.model.CityMap
import com.fagougou.government.model.Message
import com.fagougou.government.model.Speaker
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.TTS.mTts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BotMenu() {
    val scope = rememberCoroutineScope()
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
        modifier = Modifier.horizontalScroll(scrollState)
    ) {
        for (bot in botList) Column(modifier = Modifier.padding(8.dp)) {
            Surface(color = Color.Transparent) {
                HomeButton(
                    modifier = Modifier
                        .width(108.dp)
                        .height(132.dp),
                    onClick = {
                        selectedChatBot.value = bot.first
                        scope.launch(Dispatchers.IO) { startChat() }
                    },
                    contentId = botResMap[bot.first] ?: R.drawable.bot_small_unknow
                )
                if(selectedChatBot.value == bot.first)Column(
                    modifier = Modifier
                        .width(107.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    Image(painter = painterResource(R.drawable.ic_select_note), null)
                }
            }
        }
    }
}

@Composable
fun LawExpend(message: Message, index: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val isExpend = !message.isExpend
                    history[index] = message.copy(isExpend = isExpend)
                }
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = "法律依据",
                fontSize = 20.sp,
                color = Dodgerblue
            )
            val svg = if (message.isExpend)R.drawable.ic_flod else R.drawable.ic_expend
            Image(painterResource(svg), null)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ){
            if (message.isExpend) for ((i, law) in message.laws.withIndex()) {
                Text(
                    (i + 1).toString() + "." + law.name + law.position + ":",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 24.sp
                )
                Text(
                    law.content,
                    modifier = Modifier.padding(12.dp),
                    color = Color(0xFF666666),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun MessageRect(
    message: Message,
    index: Int,
    scope: CoroutineScope,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
) {
    Surface(
        shape = RoundedCornerShape(CORNER_FLOAT),
        color = backgroundColor,
    ) {
        Column {
            Text(
                modifier = Modifier.padding(12.dp),
                text = message.content + message.complex.explanation,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = textColor,
            )
            for(question in message.inlineRecommend){
                Text(
                    modifier = Modifier
                        .padding(bottom = 12.dp, start = 12.dp)
                        .clickable { scope.launch(Dispatchers.IO) { nextChat(question) } },
                    text = question,
                    fontSize = 24.sp,
                    color = Dodgerblue,
                )
            }
            if (message.laws.isNotEmpty()) Divider(
                color = Color(0xFFCCCCCC),
                thickness = 2.dp,
            )
            if (message.laws.isNotEmpty()) LawExpend(message, index)
        }
    }
}

@Composable
fun ComplexRect(
    message: Message,
    index: Int,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    navController: NavController
) {
    Surface(
        shape = RoundedCornerShape(CORNER_FLOAT),
        color = backgroundColor,
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    getComplex(message.complex.attachmentId, navController)
                }
        ) {
            Surface(color = Dodgerblue) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        message.complex.title,
                        fontSize = 24.sp,
                        color = Color.White,
                    )
                }
            }
            Text(
                modifier = Modifier.padding(12.dp),
                text = message.content + message.complex.explanation,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = textColor,
            )
            Divider(
                color = Color(0xFFCCCCCC),
                thickness = 2.dp
            )
            if (message.laws.isNotEmpty()) LawExpend(message, index)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = "点击查看更多",
                    fontSize = 20.sp,
                )
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, index: Int, scope: CoroutineScope, navController: NavController) {
    when (message.speaker) {
        Speaker.ROBOT -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) { MessageRect(message, index,scope) }
        Speaker.USER -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) { MessageRect(message, index,scope, Dodgerblue, Color.White) }
        Speaker.RECOMMEND -> Column(
            modifier = Modifier
                .padding(vertical = 12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(CORNER_FLOAT),
                color = Color.White,
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    if (message.isExpend) {
                        Row(
                            modifier = Modifier.padding(start = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (index == 1) Text("大家都在问:", fontSize = 24.sp)
                            else {
                                Image(painterResource(R.drawable.ic_relate_question), null)
                                Text(
                                    modifier = Modifier.padding(start = 12.dp),
                                    text = "相关问题",
                                    fontSize = 24.sp
                                )
                            }
                        }
                        for (question in message.recommends) Button(
                            onClick = { scope.launch(Dispatchers.IO){ nextChat(question) } },
                            content = { Text("·$question", fontSize = 24.sp, color = Dodgerblue) },
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            elevation = ButtonDefaults.elevation(0.dp)
                        )
                    } else Row(
                        modifier = Modifier
                            .clickable { history[index] = message.copy(isExpend = true) }
                    ) {
                        Image(painterResource(R.drawable.ic_relate_question), null)
                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "点击查看与您情况相关的问题",
                            fontSize = 24.sp,
                            color = Dodgerblue
                        )
                    }
                }
            }
        }
        Speaker.OPTIONS -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            when (message.option.type) {
                "radio" -> {
                    val lastIndex = message.option.items.lastIndex
                    for (y in 0..lastIndex step 4) Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (x in 0..3) {
                            val i = y + x
                            if (i <= lastIndex) {
                                val item = message.option.items[i]
                                Button(
                                    modifier = Modifier
                                        .height(60.dp)
                                        .width(180.dp),
                                    onClick = { scope.launch(Dispatchers.IO){nextChat(item)} },
                                    content = { Text(item, fontSize = 24.sp, color = Color.White) },
                                    colors = ButtonDefaults.buttonColors(Dodgerblue)
                                )
                            }
                        }
                    }
                }
                "address-with-search" -> {
                    if (currentProvince.value == "") {
                        val provinceList = CityMap.provinceList.keys.toList()
                        val lastIndex = provinceList.lastIndex
                        for (y in 0..lastIndex step 6) Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (x in 0..5) {
                                val i = y + x
                                if (y + x <= lastIndex) {
                                    Button(
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(150.dp),
                                        onClick = { currentProvince.value = provinceList[i] },
                                        content = {
                                            Text(
                                                provinceList[i],
                                                fontSize = 24.sp,
                                                color = Color.White
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(Dodgerblue)
                                    )
                                }
                            }
                        }
                    } else {
                        val cityList = CityMap.provinceList[currentProvince.value] ?: listOf()
                        val lastIndex = cityList.lastIndex
                        for (y in 0..lastIndex step 6) Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (x in 0..5) {
                                val i = y + x
                                if (i <= lastIndex) {
                                    Button(
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(150.dp),
                                        onClick = {
                                            scope.launch(Dispatchers.IO){
                                                nextChat(currentProvince.value + "-${cityList[i]}")
                                                currentProvince.value = ""
                                            }
                                        },
                                        content = {
                                            Text(
                                                cityList[i],
                                                fontSize = 20.sp,
                                                color = Color.White
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(Dodgerblue),
                                        contentPadding = PaddingValues(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Speaker.COMPLEX -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) { ComplexRect(message, index, navController = navController) }
    }
}

@Composable
fun inputBox(scope: CoroutineScope){
    if(voiceInputMode.value) Surface(
        modifier = Modifier
            .height(180.dp)
            .width(480.dp)
            .padding(8.dp),
        color = Color(0x33FFFFFF),
        shape = RoundedCornerShape(CORNER_FLOAT)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Image(
                    modifier = Modifier.clickable {
                        if(IFly.recognizeResult.value != IFly.UNWAKE_TEXT)return@clickable
                        voiceInputMode.value = false
                    },
                    painter = painterResource(id = R.drawable.ic_keyboard),
                    contentDescription = null)
                Text(
                    modifier = Modifier.padding(vertical = 16.dp),
                    text = IFly.recognizeResult.value,
                    fontSize = 21.sp,
                    color = Color.White
                )
                Image(
                    modifier = Modifier.clickable { showBotMenu.value = !showBotMenu.value },
                    painter = painterResource(id = R.drawable.ic_squad),
                    contentDescription = null)
            }
            PAG()
        }
    } else Column(
        modifier = Modifier.height(80.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(0.82f),
                value = textInputContent.value,
                onValueChange = {
                    textInputContent.value = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color(0x33FFFFFF),
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 21.sp),
                shape = RoundedCornerShape(CORNER_FLOAT),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        val content = textInputContent.value
                        textInputContent.value = ""
                        scope.launch(Dispatchers.IO) { nextChat(content) }
                    }
                )
            )
            Button(
                modifier = Modifier.padding(start = 24.dp),
                content = { Image(painterResource(R.drawable.ic_microphone),null) },
                onClick = { voiceInputMode.value = true },
                colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                shape = RoundedCornerShape(50)
            )
            Button(
                modifier = Modifier.padding(start = 24.dp),
                content = { Image(painterResource(R.drawable.ic_squad),null) },
                onClick = { showBotMenu.value = !showBotMenu.value },
                colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                shape = RoundedCornerShape(50)
            )
        }
    }
}

@Composable
fun ChatPage(navController: NavController) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            Header(
                "智能咨询(${selectedChatBot.value})",
                navController,
                onBack = {
                    IFly.wakeMode()
                    mTts.stopSpeaking()
                    textInputContent.value = ""
                    history.clear()
                    currentProvince.value = ""
                }
            )
            var lazyHeight = 850 - if(showBotMenu.value) 135 else 0
            lazyHeight -= if(voiceInputMode.value) 100 else 0
            LazyColumn(
                modifier = Modifier
                    .height(lazyHeight.dp)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Top,
                state = listState,
            ) {
                item {
                    Row(Modifier.padding(8.dp)) {
                        Surface(
                            color = Color(0x33FFFFFF),
                            shape = RoundedCornerShape(CORNER_FLOAT)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Image(painterResource(R.drawable.ic_note),null)
                                Text("温馨提示：拿起话筒后声音更清晰")
                            }
                        }
                    }
                }
                items(history.size) { index ->
                    MessageItem(history[index], index, scope, navController)
                }
                if (history.lastOrNull()?.speaker==Speaker.USER) item {
                    MessageItem(Message(Speaker.ROBOT, content = ". . ."), -1, scope, navController)
                }
                item { Row( Modifier .fillMaxWidth() .height(20.dp)) {} }
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            inputBox(scope)
            if(showBotMenu.value) BotMenu()
        }
    }
    BackHandler(enabled = true) {
    }
}
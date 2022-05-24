package com.fagougou.government.chatPage

import android.text.TextUtils
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.chatPage.ChatViewModel.botQueryIdMap
import com.fagougou.government.chatPage.ChatViewModel.currentProvince
import com.fagougou.government.chatPage.ChatViewModel.getComplex
import com.fagougou.government.chatPage.ChatViewModel.getDefInfo
import com.fagougou.government.chatPage.ChatViewModel.history
import com.fagougou.government.chatPage.ChatViewModel.listState
import com.fagougou.government.chatPage.ChatViewModel.nextChat
import com.fagougou.government.chatPage.ChatViewModel.selectedChatBot
import com.fagougou.government.chatPage.ChatViewModel.showBotMenu
import com.fagougou.government.chatPage.ChatViewModel.startChat
import com.fagougou.government.chatPage.ChatViewModel.textInputContent
import com.fagougou.government.chatPage.ChatViewModel.voiceInputMode
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel.constWechatUrl
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.homePage.HomeButton
import com.fagougou.government.model.CityMap
import com.fagougou.government.model.Message
import com.fagougou.government.model.Speaker
import com.fagougou.government.repo.Client
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.IFly.wakeMode
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.SafeBack.safeBack
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips
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
                        if(Client.globalLoading.value <= 0) { with(DialogViewModel){
                            clear()
                            title = "温馨提示"
                            firstButtonText.value = "取消"
                            firstButtonOnClick.value = { content.value = "" }
                            secondButtonText.value = "确定"
                            secondButtonOnClick.value = {
                                content.value = ""
                                selectedChatBot.value = bot.first
                                scope.launch(Dispatchers.IO) { startChat() }
                            }
                            content.value = "更换领域后，当前的记录会清除"
                        } }
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
fun LawExpend(message: Message, index: Int, scope: CoroutineScope) {
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
            if ( message.listDef.size==0){
                Text(
                modifier = Modifier.padding(12.dp),
                text = message.content + message.complex.explanation,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = textColor,
            )
            }else{
                val annotatedString = buildAnnotatedString {

                    message.listDef.forEach(){
                        if (!TextUtils.isEmpty(it.content)){
                            if (it.style==0){
                                append(it.content)
                            }else{
                                pushStringAnnotation(tag = "policy", annotation = it.content)
                                withStyle(style = SpanStyle(color = Dodgerblue)) {
                                    append(" ["+it.content+"] ")
                                }
                                pop()
                            }
                        }

                    }
                }

                ClickableText(
                    modifier = Modifier.padding(12.dp),
                    text = annotatedString, style = MaterialTheme.typography.h5, onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "policy", start = offset, end = offset).firstOrNull()?.let {
                            Log.d("policy URL", it.item)

                            getDefInfo(it.item);
                        }
                    })
            }





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
            if (message.laws.isNotEmpty()) LawExpend(message, index, scope)
        }
    }
}

@Composable
fun ComplexRect(
    message: Message,
    index: Int,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    navController: NavController,
    scope: CoroutineScope
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
                ) { BasicText(message.complex.title) }
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
            if (message.laws.isNotEmpty()) LawExpend(message, index, scope)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    color=Color(0xFF0E7AE6),
                    text = "点击查看",
                    fontSize = 20.sp,
                )
                val svg = R.drawable.ic_icon_right_mark
                Image(painterResource(svg), null)
            }
        }
    }
}

@Composable
fun MessageItem(message: Message, index: Int, scope: CoroutineScope, navController: NavController) {
    when (message.speaker) {
        Speaker.ROBOT -> Row(
            modifier = Modifier
                .fillMaxWidth(0.75f)
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
                .fillMaxWidth(0.75f)
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
                            .clickable {
                                history[index] = message.copy(isExpend = true)
                                if(index == history.lastIndex) scope.launch(Dispatchers.Main) {
                                    listState.scrollToItem(index+2)
                                }
                            }
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
                                    content = { BasicText(item,0.dp,20.sp) },
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
                                        content = { BasicText(provinceList[i]) },
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
                                        content = { BasicText(cityList[i],0.dp,20.sp ) },
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
                .fillMaxWidth(0.75f)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) { ComplexRect(message, index, navController = navController,scope = scope) }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
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
                    modifier = Modifier.clickable {
                        showBotMenu.value = true
                        voiceInputMode.value = false
                    },
                    painter = painterResource(id = R.drawable.ic_squad),
                    contentDescription = null)
            }
            PAG()
        }
    } else Column(
        modifier = Modifier.height(80.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        val (text,bot) = remember{ FocusRequester.createRefs() }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .focusRequester(text)
                    .onFocusChanged { state -> if (state.isFocused) showBotMenu.value = false }
                    .focusable(),
                value = textInputContent.value,
                onValueChange = {
                    textInputContent.value = it
                    Router.lastTouchTime = Time.stampL
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color(0x33FFFFFF),
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 21.sp),
                shape = RoundedCornerShape(50),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        val content = textInputContent.value
                        textInputContent.value = ""
                        scope.launch(Dispatchers.IO) { nextChat(content) }
                    }
                ),
            )
            Button(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .width(54.dp)
                    .height(54.dp),
                content = { Image(painterResource(R.drawable.ic_microphone),null) },
                onClick = {
                    showBotMenu.value = false
                    voiceInputMode.value = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(8.dp)
            )
            Button(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .width(54.dp)
                    .height(54.dp)
                    .focusRequester(bot)
                    .focusable(),
                content = { Image(painterResource(R.drawable.ic_squad),null) },
                onClick = {
                    showBotMenu.value = !showBotMenu.value
                    voiceInputMode.value = false
                    text.freeFocus()
                    bot.requestFocus()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(8.dp)
            )
        }
    }
}

@Composable
fun ChatPage(navController: NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        wakeMode()
    }
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
                {
                    with(DialogViewModel){
                        clear()
                        title = "温馨提示"
                        content.value = "请确认本次咨询是否解决您的问题？"
                        firstButtonText.value = "已经解决"
                        firstButtonOnClick.value = {
                            content.value = ""
                            IFly.stopAll()
                            navController.safeBack()
                        }
                        secondButtonText.value = "没有解决，转人工"
                        secondButtonOnClick.value = {
                            content.value = ""
                            IFly.stopAll()
                            ImSdkUtils.startAc(Tips.context)
                        }
                        content.value = "请确认本次咨询是否解决您的问题？"
                    }
                },
                false,
                constWechatUrl
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
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = Color(0x33FFFFFF),
                            shape = RoundedCornerShape(CORNER_FLOAT)
                        ) {
                            Row(
                                Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Image(painterResource(R.drawable.ic_note),null)
                                Spacer(
                                    Modifier
                                        .width(16.dp)
                                        .height(12.dp))
                                BasicText("温馨提示：拿起话筒后声音更清晰",0.dp,20.sp)
                            }
                        }
                    }
                }
                items(history.size) { index -> MessageItem(history[index], index, scope, navController) }
                if (history.lastOrNull()?.speaker==Speaker.USER) item {
                    MessageItem(Message(Speaker.ROBOT, content = ". . ."), -1, scope, navController)
                }
                item { Row( Modifier .fillMaxWidth() .height(60.dp)) {} }
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
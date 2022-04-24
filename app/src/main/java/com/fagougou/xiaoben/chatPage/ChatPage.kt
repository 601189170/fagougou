package com.fagougou.xiaoben.chatPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.chatPage.ChatPage.botQueryIdMap
import com.fagougou.xiaoben.chatPage.ChatPage.history
import com.fagougou.xiaoben.chatPage.ChatPage.listState
import com.fagougou.xiaoben.chatPage.ChatPage.nextChat
import com.fagougou.xiaoben.chatPage.ChatPage.selectedChatBot
import com.fagougou.xiaoben.chatPage.ChatPage.startChat
import com.fagougou.xiaoben.homePage.HomeButton
import com.fagougou.xiaoben.model.*
import com.fagougou.xiaoben.repo.Client.retrofitClient
import com.fagougou.xiaoben.ui.theme.CORNER_PERCENT
import com.fagougou.xiaoben.ui.theme.Dodgerblue
import com.fagougou.xiaoben.utils.IFly
import com.fagougou.xiaoben.utils.TTS
import com.fagougou.xiaoben.utils.TTS.mTts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ChatPage {
    val history = mutableStateListOf<Message>()
    val selectedChatBot = mutableStateOf("小笨")
    var sessionId = ""
    var botQueryIdMap = mutableMapOf<String, String>()
    val listState = LazyListState()
    var tempQueryId = ""

    suspend fun addChatData(chatData: ChatData) {
        if (history.lastOrNull()?.speaker == Speaker.OPTIONS) history.last().isExpend = false
        for (say in chatData.botSays) {
            when (say.type) {
                "text" -> {
                    val content = say.content.body.replace("question::", "").replace("def::", "")
                        .replace("#", "")
                    history.add(Message(Speaker.ROBOT, content = content, laws = say.content.laws))
                    TTS.speak(content)
                }
                "hyperlink" -> {
                    history.add(
                        Message(
                            Speaker.ROBOT,
                            content = say.content.description + say.content.url
                        )
                    )
                    TTS.speak(say.content.description)
                }
                "complex" -> {

                }
            }
            if (say.recommends.isNotEmpty()) history.add(
                Message(
                    Speaker.RECOMMEND,
                    recommends = say.recommends,
                    isExpend = true
                )
            )
            if (say.content.queryRecordItemId != "") {
                tempQueryId = say.content.queryRecordItemId
                if (say.content.title == "相关问题" || say.isAnswered) {
                    getRelate(say.content.queryRecordItemId)
                }
            }
        }
        if (chatData.option.items.isNotEmpty()) history.add(
            Message(
                Speaker.OPTIONS,
                option = chatData.option,
                isExpend = true
            )
        )
        withContext(Dispatchers.Main) {
            listState.scrollToItem(history.lastIndex)
        }

    }

    fun startChat() {
        TTS.stopSpeaking()
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                retrofitClient.startChat(botQueryIdMap[selectedChatBot.value] ?: "").execute()
            val body = response.body() ?: return@launch
            sessionId = body.chatData.queryId
            addChatData(body.chatData)
        }
    }

    fun nextChat(message: String) {
        TTS.stopSpeaking()
        history.add(Message(Speaker.USER, message))
        CoroutineScope(Dispatchers.Main).launch {
            listState.scrollToItem(history.lastIndex)
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = retrofitClient.nextChat(sessionId, ChatRequest(message)).execute()
                val body = response.body() ?: return@launch
                addChatData(body.chatData)
            } catch (e: Exception) {
                addChatData(
                    ChatData(
                        botSays = listOf(
                            BotSay(
                                "text",
                                BotSaysContent(
                                    body = "抱歉，您的查询暂无相关数据哦，建议扩大查询范围，或修改查询条件试试看。",
                                    queryRecordItemId = tempQueryId
                                )
                            )
                        )
                    )
                )
            }
        }
    }

    fun getRelate(queryRecordItemId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitClient.relateQuestion(queryRecordItemId).execute()
            val body = response.body() ?: return@launch
            for (say in body.data.says) history.add(
                Message(
                    Speaker.RECOMMEND,
                    recommends = say.content.questions
                )
            )
            withContext(Dispatchers.Main) {
                listState.scrollToItem(history.lastIndex)
            }
        }
    }

}

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
                    .width(182.dp)
                    .height(228.dp),
                onClick = {
                    selectedChatBot.value = bot.first
                    startChat()
                },
                contentId = botResMap[bot.first] ?: R.drawable.bot_small_unknow
            )
        }
    }
    startChat()
}

@Composable
fun LawExpend(message: Message) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (message.isExpend) for ((i, law) in message.laws.withIndex()) {
            Text(
                (i + 1).toString() + "." + law.name + law.position + ":",
                modifier = Modifier.padding(12.dp),
                fontSize = 25.sp
            )
            Text(
                law.content,
                modifier = Modifier.padding(12.dp),
                color = Color(0xFF666666),
                fontSize = 22.sp
            )
        } else Button(
            onClick = { message.isExpend = true },
            content = {
                Row {
                    Text(
                        modifier = Modifier.padding(end = 24.dp),
                        text = "点击查看法律依据",
                        fontSize = 28.sp,
                        color = Dodgerblue
                    )
                    Image(
                        painterResource(R.drawable.ic_expend),
                        null
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            elevation = ButtonDefaults.elevation(0.dp)
        )
    }
}

@Composable
fun MessageRect(
    message: Message,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
) {
    Surface(
        shape = RoundedCornerShape(CORNER_PERCENT),
        color = backgroundColor,
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                message.content,
                fontSize = 28.sp,
                color = textColor,
            )
            if (message.laws.isNotEmpty()) Divider(
                modifier = Modifier.padding(18.dp),
                color = Color(0xFFCCCCCC),
                thickness = 2.dp,
                startIndent = 10.dp
            )
            if (message.laws.isNotEmpty()) LawExpend(message)
        }
    }
}


@Composable
fun MessageItem(message: Message, scope: CoroutineScope, listState: LazyListState) {
    when (message.speaker) {
        Speaker.ROBOT -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) { MessageRect(message) }
        Speaker.USER -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) { MessageRect(message, Dodgerblue, Color.White) }
        Speaker.RECOMMEND -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(CORNER_PERCENT),
                color = Color.White,
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    if (message.isExpend) for (question in message.recommends) Button(
                        onClick = { nextChat(question) },
                        content = { Text(question, fontSize = 28.sp, color = Dodgerblue) },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) else Button(
                        onClick = { message.isExpend = true },
                        content = {
                            Row {
                                Image(painterResource(R.drawable.ic_relate_question), null)
                                Text(
                                    modifier = Modifier.padding(start = 24.dp),
                                    text = "点击查看与您情况相关的问题",
                                    fontSize = 28.sp,
                                    color = Dodgerblue
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        elevation = ButtonDefaults.elevation(0.dp)
                    )
                }
            }

        }
        Speaker.OPTIONS -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (message.option.type) {
                "radio" -> {
                    if (message.isExpend) for (item in message.option.items) Button(
                        onClick = { nextChat(item) },
                        content = { Text(item, fontSize = 28.sp) },
                    )
                }
                "address-with-search" -> {

                }
            }
        }
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
                mTts.stopSpeaking()
                history.clear()
            }
        )
        BotMenu()
        val scope = rememberCoroutineScope()
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .padding(horizontal = 36.dp),
            verticalArrangement = Arrangement.Top,
            state = listState,
        ) {
            items(history) { message -> MessageItem(message, scope, listState) }
            item{
                Row(modifier = Modifier.fillMaxWidth().height(300.dp)){}
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(IFly.volumeState.value, fontSize = 32.sp, color = Color.White)
            Text(IFly.recognizeResult.value, fontSize = 32.sp, color = Color.White)
        }
    }
}

@Preview
@Composable
fun ComposablePreview() {
    Surface(
        color = Color.White,
    ) {
    }
}
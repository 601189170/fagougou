package com.fagougou.xiaoben.chatPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.chatPage.ChatPage.chatBotMap
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ChatPage {
    val history = mutableStateListOf<Message>()
    val selectedChatBot = mutableStateOf("小笨")
    var sessionId = ""
    var chatBotMap = mutableMapOf<String, String>()
    var tyBotMap = mutableMapOf<String, String>()
    val listState = LazyListState()
    var tempQueryId = ""

    suspend fun addChatData(chatData: ChatData){
        for (say in chatData.botSays) {
            when(say.type){
                "text" -> {
                    val content = say.content.body.replace("question::","").replace("def::","").replace("#","")
                    history.add(Message(Speaker.ROBOT, content = content))
                    TTS.speak(content)
                }
                "hyperlink" -> {
                    history.add(Message(Speaker.ROBOT, content = say.content.description+say.content.url))
                    TTS.speak(say.content.description)
                }
            }
            if (say.recommends.isNotEmpty()) history.add(Message(Speaker.RECOMMEND, recommends = say.recommends))
            if (say.content.laws.isNotEmpty()) history.add(Message(Speaker.LAW, laws = say.content.laws))
            if (say.content.queryRecordItemId != ""){
                tempQueryId = say.content.queryRecordItemId
                if(say.content.title == "相关问题" || say.isAnswered){
                    history.add(Message(Speaker.ROBOT, content = "我们找到了与您情况相关的一些问题"))
                    TTS.speak("我们找到了与您情况相关的一些问题")
                    getRelate(say.content.queryRecordItemId)
                }
            }
        }
        if (chatData.option.items.isNotEmpty()) history.add(Message(Speaker.OPTIONS, option = chatData.option))
        withContext(Dispatchers.Main){
            listState.scrollToItem(history.lastIndex)
        }

    }

    fun startChat() {
        TTS.stopSpeaking()
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitClient.startChat(chatBotMap[selectedChatBot.value] ?: "").execute()
            val body = response.body() ?: return@launch
            sessionId = body.chatData.queryId
            addChatData(body.chatData)
        }
    }

    fun nextChat(message:String) {
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
            }catch (e:Exception){
                addChatData(ChatData(
                    botSays = listOf(BotSay("text",
                        BotSaysContent(
                            body = "抱歉，您的查询暂无相关数据哦，建议扩大查询范围，或修改查询条件试试看。",
                            queryRecordItemId = tempQueryId
                        )
                    ))
                ))
            }
        }
    }

    fun getRelate(queryRecordItemId:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitClient.relateQuestion(queryRecordItemId).execute()
            val body = response.body() ?: return@launch
            for(say in body.data.says)history.add(Message(Speaker.RECOMMEND, recommends = say.content.questions))
            withContext(Dispatchers.Main){
                listState.scrollToItem(history.lastIndex)
            }
        }
    }

}

@Composable
fun BotMenu() {
    val botList = chatBotMap.toList()
    val menuList = listOf(
        botList.subList(0, botList.size / 2),
        botList.subList((botList.size) / 2, botList.size),
    )
    for (rowList in menuList) Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (bot in rowList) Button(
            onClick = {
                selectedChatBot.value = bot.first
                startChat()
            },
            content = { Text(bot.first, fontSize = 18.sp) }
        )
    }
}

@Composable
fun Avatar(id: Int) {
    Image(
        modifier = Modifier
            .clip(CircleShape)
            .size(48.dp),
        painter = painterResource(id),
        contentDescription = "Robot Avatar"
    )
}

@Composable
fun MessageRect(
    string: String,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .shadow(2.dp, shape = RoundedCornerShape(CORNER_PERCENT), false),
        shape = RoundedCornerShape(CORNER_PERCENT),
        color = backgroundColor,
    ) {
        Text(
            string,
            fontSize = 32.sp,
            modifier = Modifier.padding(12.dp),
            color = textColor,
        )
    }
}

@Composable
fun MessageItem(message: Message, scope: CoroutineScope, listState:LazyListState) {
    when (message.speaker) {
        Speaker.ROBOT -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(R.drawable.fagougou)
            MessageRect(message.content)
        }
        Speaker.USER -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MessageRect(message.content, Dodgerblue, Color.White)
            Avatar(R.drawable.fagougou)
        }
        Speaker.RECOMMEND -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        ){
            for (question in message.recommends) Button(
                modifier = Modifier.padding(start = 72.dp, top = 12.dp, bottom = 12.dp),
                onClick = {
                    nextChat(question)
                },
                content = {
                    Text(question, fontSize = 28.sp)
                }
            )
        }
        Speaker.LAW -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        ){
            for (law in message.laws) Text(
                law.name+law.position+":"+law.content,
                modifier = Modifier.padding(start = 72.dp, top = 12.dp, bottom = 12.dp),
            )
        }
        Speaker.OPTIONS -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            when(message.option.type){
                "radio" -> {
                    for (item in message.option.items) Button(
                        onClick = { nextChat(item) },
                        content={ Text(item, fontSize = 28.sp) },
                    )
                }
                "address-with-search" ->{

                }
            }
        }
    }
}

@Composable
fun ChatPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.86f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    content = { Text("返回", fontSize = 32.sp) }
                )
                Text(
                    "智能咨询(${selectedChatBot.value}${chatBotMap[selectedChatBot.value]})",
                    fontSize = 32.sp
                )
                Surface { }
            }
            BotMenu()
        }
        val scope = rememberCoroutineScope()
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.86f)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Top,
            state = listState,
        ) {
            items(history.size) { index -> MessageItem(history[index], scope, listState) }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(IFly.volumeState.value, fontSize = 32.sp)
            Text(IFly.recognizeResult.value, fontSize = 32.sp)
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
package com.fagougou.xiaoben.chatPage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.fagougou.xiaoben.CommonApplication.Companion.TAG
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.chatPage.ChatPage.chatBotMap
import com.fagougou.xiaoben.chatPage.ChatPage.history
import com.fagougou.xiaoben.chatPage.ChatPage.selectedChatBot
import com.fagougou.xiaoben.chatPage.ChatPage.startChat
import com.fagougou.xiaoben.model.Message
import com.fagougou.xiaoben.model.Speaker
import com.fagougou.xiaoben.repo.Client.retrofitClient
import com.fagougou.xiaoben.ui.theme.CORNER_PERCENT
import com.fagougou.xiaoben.ui.theme.Dodgerblue
import com.fagougou.xiaoben.utils.IFly
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ChatPage {
    val history = mutableStateListOf<Message>()
    val selectedChatBot = mutableStateOf("小笨")
    var chatBotMap = mutableMapOf<String,String>()
    var tyBotMap = mutableMapOf<String,String>()

    fun startChat(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitClient.startChat(chatBotMap[selectedChatBot.value]?:"").execute()
            val body = response.body() ?: return@launch
            for(say in body.startChatData.botSays) history.add(Message(Speaker.ROBOT,say.content.body,say.recommendQA))
        }
    }
}

@Composable
fun BotMenu() {
    val botList = chatBotMap.toList()
    val menuList = listOf(
        botList.subList(0,botList.size/2),
        botList.subList((botList.size)/2,botList.size),
    )
    for (rowList in menuList) Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for(bot in rowList)Button(
            onClick = {
                selectedChatBot.value = bot.first
                startChat()
            },
            content = { Text(bot.first,fontSize = 18.sp) }
        )
    }
}

@Composable
fun Avatar(id:Int) {
    Image(
        modifier = Modifier
            .clip(CircleShape)
            .size(48.dp),
        painter = painterResource(id),
        contentDescription = "Robot Avatar"
    )
}

@Composable
fun MessageRect(string: String, backgroundColor: Color = Color.White, textColor: Color = Color.Black) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .shadow(2.dp, shape = RoundedCornerShape(CORNER_PERCENT),false),
        shape = RoundedCornerShape(CORNER_PERCENT),
        color = backgroundColor,
    ){
        Text(
            string,
            fontSize = 32.sp,
            modifier = Modifier.padding(8.dp),
            color = textColor,
        )
    }
}

@Composable
fun MessageItem(message: Message) {
    if(message.speaker == Speaker.ROBOT) Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(R.drawable.fagougou)
        MessageRect(message.content)
    } else Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MessageRect(message.content,Dodgerblue,Color.White)
        Avatar(R.drawable.fagougou)
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
    ){
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
                    content = { Text("返回",fontSize = 32.sp) }
                )
                Text("智能咨询(${selectedChatBot.value}${chatBotMap[selectedChatBot.value]})",fontSize = 32.sp)
                Surface { }
            }
            BotMenu()
        }
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.86f)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Top,
            state = listState,
        ) {
            items(history.size){ index ->
                MessageItem(history[index])
                for (question in history[index].recommendQA) Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        history.add(Message(Speaker.USER,question))
                        scope.launch {
                            listState.scrollToItem(history.lastIndex)
                        }
                    },
                    content = {
                        Text(question, fontSize = 28.sp)
                    }
                )
            }
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
        Column{
            for(i in 0..4){
                MessageItem(Message(Speaker.ROBOT,"aaaasdfd", listOf("sdfes","sdfews")))
                MessageItem(Message(Speaker.USER,"aaaasdfd"))
            }
        }
    }

}
package com.fagougou.xiaoben.chatPage

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.chatPage.ChatPage.chatBotMap
import com.fagougou.xiaoben.chatPage.ChatPage.history
import com.fagougou.xiaoben.chatPage.ChatPage.selectedChatBot
import com.fagougou.xiaoben.chatPage.ChatPage.startChat
import com.fagougou.xiaoben.model.Message
import com.fagougou.xiaoben.model.Speaker
import com.fagougou.xiaoben.repo.Client.retrofitClient
import com.fagougou.xiaoben.utils.IFly
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            for(say in body.startChatData.botSays) history.add(Message(Speaker.ROBOT,say.content.body))
        }
    }
}

@Composable
fun ChatPage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ){
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    content = { Text("返回",fontSize = 32.sp) }
                )
                Text("智能咨询(${selectedChatBot.value}${chatBotMap[selectedChatBot.value]})",fontSize = 32.sp)
                Surface { }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for(bot in chatBotMap)Button(
                    onClick = {
                        selectedChatBot.value = bot.key
                        startChat()
                    },
                    content = { Text(bot.key,fontSize = 24.sp) }
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Top
        ) {
            for (message in history) Text(message.content, fontSize = 32.sp)
        }
        Column(
            modifier = Modifier.fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(IFly.volumeState.value, fontSize = 32.sp)
            Text(IFly.recognizeResult.value, fontSize = 32.sp)
        }
    }
}
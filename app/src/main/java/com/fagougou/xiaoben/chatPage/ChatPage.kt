package com.fagougou.xiaoben.chatPage

import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.xiaoben.CommonApplication.Companion.TAG
import com.fagougou.xiaoben.CommonApplication.Companion.context
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.chatPage.ChatPage.botQueryIdMap
import com.fagougou.xiaoben.chatPage.ChatPage.history
import com.fagougou.xiaoben.chatPage.ChatPage.ioState
import com.fagougou.xiaoben.chatPage.ChatPage.listState
import com.fagougou.xiaoben.chatPage.ChatPage.nextChat
import com.fagougou.xiaoben.chatPage.ChatPage.selectedChatBot
import com.fagougou.xiaoben.chatPage.ChatPage.startChat
import com.fagougou.xiaoben.homePage.HomeButton
import com.fagougou.xiaoben.model.*
import com.fagougou.xiaoben.repo.Client.apiService
import com.fagougou.xiaoben.repo.Client.handleException
import com.fagougou.xiaoben.ui.theme.CORNER_PERCENT
import com.fagougou.xiaoben.ui.theme.Dodgerblue
import com.fagougou.xiaoben.utils.IFly
import com.fagougou.xiaoben.utils.IFly.UNWAKE_TEXT
import com.fagougou.xiaoben.utils.MMKV
import com.fagougou.xiaoben.utils.TTS
import com.fagougou.xiaoben.utils.TTS.mTts
import kotlinx.coroutines.*
import org.libpag.PAGFile
import org.libpag.PAGView

object ChatPage {
    val history = mutableStateListOf<Message>()
    val selectedChatBot = mutableStateOf("小笨")
    var sessionId = ""
    var botQueryIdMap = mutableMapOf<String, String>()
    val listState = LazyListState()
    var tempQueryId = ""
    val ioState = mutableStateOf(false)

    suspend fun addChatData(chatData: ChatData) {
        for (say in chatData.botSays) {
            val content = say.content.body.replace("question::", "").replace("def::", "").replace("#", "")
            when (say.type) {
                "text" -> {
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
                    history.add(Message(Speaker.COMPLEX, content = content, complex = say.content))
                    TTS.speak(content)
                    getComplex(say.content.attachmentId)
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
        history.clear()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tokenResponse = apiService.auth(AuthRequest()).execute()
                val tokenBody = tokenResponse.body() ?: Auth()
                MMKV.kv.encode("token", tokenBody.data.token)
                val response = apiService.startChat(botQueryIdMap[selectedChatBot.value] ?: "").execute()
                val body = response.body() ?: return@launch
                sessionId = body.chatData.queryId
                addChatData(body.chatData)
            }catch (e:Exception){
                handleException(e)
            }
        }
    }

    fun nextChat(message: String) {
        TTS.stopSpeaking()
        if(message.isBlank())return
        if (history.lastOrNull()?.speaker == Speaker.OPTIONS) history.removeLastOrNull()
        history.add(Message(Speaker.USER, message))
        CoroutineScope(Dispatchers.Main).launch {
            listState.scrollToItem(history.lastIndex)
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ioState.value = true
                val fixMessage = if (message.last()=='。')message.dropLast(1) else message
                val response = apiService.nextChat(sessionId, ChatRequest(fixMessage)).execute()
                val body = response.body() ?: return@launch
                addChatData(body.chatData)
            } catch (e: Exception) {
                Log.e(TAG,e.stackTraceToString())
            }finally {
                ioState.value = false
            }
        }
    }

    fun getRelate(queryRecordItemId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.relateQuestion(queryRecordItemId).execute()
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

    fun getComplex(attachmentId:String){
        CoroutineScope(Dispatchers.IO).launch {
            apiService.attachment(attachmentId).execute()
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
    startChat()
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
            onClick = { history[index] = message.copy(isExpend = true) },
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
    index:Int,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
) {
    Surface(
        shape = RoundedCornerShape(CORNER_PERCENT),
        color = backgroundColor,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                message.content+message.complex.explanation,
                fontSize = 28.sp,
                color = textColor,
            )
            if (message.laws.isNotEmpty()) Divider(
                modifier = Modifier.padding(horizontal = 18.dp),
                color = Color(0xFFCCCCCC),
                thickness = 2.dp,
                startIndent = 10.dp
            )
            if (message.laws.isNotEmpty()) LawExpend(message,index)
        }
    }
}


@Composable
fun MessageItem(message: Message,index:Int, scope: CoroutineScope, listState: LazyListState) {
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
                .padding(vertical = 12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(CORNER_PERCENT),
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
                    } else Button(
                        onClick = {
                            history[index] = message.copy(isExpend = true)
                        },
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
                    for (item in message.option.items) Button(
                        onClick = { nextChat(item) },
                        content = {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = item,
                                fontSize = 30.sp,
                                color = Color.White
                            )
                        },
                        colors = ButtonDefaults.buttonColors(Dodgerblue)
                    )
                }
                "address-with-search" -> {

                }
            }
        }
        Speaker.COMPLEX -> Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) { MessageRect(message,index) }
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
            items(history.size) { index -> MessageItem(history[index],index, scope, listState) }
            if(ioState.value) item { MessageItem(Message(Speaker.ROBOT, content = ". . ."),-1, scope, listState) }
            item{
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)){}
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
}

@Composable
fun PAG(){
    Surface(color = Color.Transparent) {
        AndroidView(
            modifier = Modifier
                .height(128.dp)
                .width(512.dp),
            factory = {
                PAGView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val pagFile = PAGFile.Load(context.assets, "chat_unwaked.pag")
                    composition = pagFile
                    setRepeatCount(0)
                    play()
                    val it = this
                    CoroutineScope(Dispatchers.Default).launch {
                        var alpha = 0
                        while (true) {
                            delay(50)
                            when (IFly.recognizeResult.value) {
                                UNWAKE_TEXT -> if(alpha<100)alpha+=10
                                else -> if (alpha>1)alpha-=10
                            }
                            it.alpha = alpha.toFloat() / 100f
                        }
                    }
                }
            }
        )
        AndroidView(
            modifier = Modifier
                .height(128.dp)
                .width(512.dp),
            factory = {
                PAGView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val pagFile = PAGFile.Load(context.assets, "chat_waked.pag")
                    composition = pagFile
                    setRepeatCount(0)
                    play()
                    val it = this
                    CoroutineScope(Dispatchers.Default).launch {
                        var alpha = 0
                        while (true) {
                            delay(50)
                            when (IFly.recognizeResult.value) {
                                UNWAKE_TEXT -> if(alpha>1)alpha-=10
                                else -> if (alpha<100)alpha+=10
                            }
                            it.alpha = alpha.toFloat() / 100f
                        }
                    }
                }
            }
        )
    }
}
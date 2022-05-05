package com.fagougou.xiaoben.chatPage

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.fagougou.xiaoben.CommonApplication
import com.fagougou.xiaoben.model.*
import com.fagougou.xiaoben.repo.Client
import com.fagougou.xiaoben.utils.MMKV
import com.fagougou.xiaoben.utils.TTS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ChatViewModel {
    val history = mutableStateListOf<Message>()
    val selectedChatBot = mutableStateOf("小笨")
    var sessionId = ""
    var botQueryIdMap = mutableMapOf<String, String>()
    val listState = LazyListState()
    val chatIoState = mutableStateOf(false)
    var needAddressNow = mutableStateOf(false)
    var currentProvince = mutableStateOf("")

    suspend fun addChatData(chatData: ChatData) {
        val defRegex = Regex("#def::.*?#")

        for (say in chatData.botSays) {
            val content =say.content.body.replace(defRegex, "")
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
                }
            }
            if (say.recommends.isNotEmpty()) history.add(
                Message(
                    Speaker.RECOMMEND,
                    recommends = say.recommends,
                    isExpend = true
                )
            )
            if (say.content.title == "相关问题" || say.isAnswered) {
                getRelate(say.content.queryRecordItemId)
            }
        }
        if (chatData.option.items.isNotEmpty()) history.add(
            Message(
                Speaker.OPTIONS,
                option = chatData.option,
                isExpend = true
            )
        )
        if (chatData.option.type=="address-with-search") history.add(
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
                val tokenResponse = Client.apiService.auth(AuthRequest()).execute()
                val tokenBody = tokenResponse.body() ?: Auth()
                MMKV.kv.encode("token", tokenBody.data.token)
                val response =
                    Client.apiService.startChat(botQueryIdMap[selectedChatBot.value] ?: "")
                        .execute()
                val body = response.body() ?: return@launch
                sessionId = body.chatData.queryId
                addChatData(body.chatData)
            } catch (e: Exception) {
                Client.handleException(e)
            }
        }
    }

    fun nextChat(message: String) {
        TTS.stopSpeaking()
        if (message.isBlank()) return
        if (history.lastOrNull()?.speaker == Speaker.OPTIONS) history.removeLastOrNull()
        history.add(Message(Speaker.USER, message))
        CoroutineScope(Dispatchers.Main).launch {
            listState.scrollToItem(history.lastIndex)
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                chatIoState.value = true
                var fixMessage = if (message.last() == '。') message.dropLast(1) else message
                fixMessage = fixMessage
                    .replace("嗯，", "")
                    .replace("额，", "")
                    .replace("嗯", "")
                    .replace("额", "")
                    .replace("啊", "")
                    .replace(" ", "")
                val response =
                    Client.apiService.nextChat(sessionId, ChatRequest(fixMessage)).execute()
                val body = response.body() ?: return@launch
                addChatData(body.chatData)
            } catch (e: Exception) {
                Log.e(CommonApplication.TAG, e.stackTraceToString())
            } finally {
                chatIoState.value = false
            }
        }
    }

    fun getRelate(queryRecordItemId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = Client.apiService.relateQuestion(queryRecordItemId).execute()
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

    fun getComplex(attachmentId: String, navController: NavController) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = Client.apiService.attachment(attachmentId).execute()
            val outerBody = response.body() ?: AttachmentResponse()
            val content = outerBody.data.content
            Complex.bodyList.clear()
            Complex.bodyList.addAll(content.body)
            Complex.caseList.clear()
            Complex.caseList.addAll(content.cases)
            withContext(Dispatchers.Main) { navController.navigate("complex") }
        }
    }
}
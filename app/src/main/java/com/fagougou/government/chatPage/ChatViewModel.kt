package com.fagougou.government.chatPage

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.fagougou.government.model.*
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.utils.InlineRecommend.getInline
import com.fagougou.government.utils.InlineRecommend.removeInline
import com.fagougou.government.utils.MMKV
import com.fagougou.government.utils.TTS
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
    var currentProvince = mutableStateOf("")
    val voiceInputMode = mutableStateOf(true)
    val textInputContent = mutableStateOf("")
    val showBotMenu = mutableStateOf(false)

    suspend fun addChatData(chatData: ChatData) {
        for (say in chatData.botSays) {
            val content = say.content.body.removeInline()
            val inlineRecommend = say.content.body.getInline()
            when (say.type) {
                "text" -> {
                    history.add(
                        Message(
                            Speaker.ROBOT,
                            inlineRecommend = inlineRecommend,
                            content = content,
                            laws = say.content.laws
                        )
                    )
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
        if (chatData.option.type == "address-with-search") history.add(
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

    suspend fun startChat() {
        TTS.stopSpeaking()
        history.clear()
        try {
            val tokenResponse = Client.apiService.auth(AuthRequest()).execute()
            val tokenBody = tokenResponse.body() ?: Auth()
            MMKV.kv.encode("token", tokenBody.data.token)
            val response = Client.apiService
                .startChat(botQueryIdMap[selectedChatBot.value] ?: "").execute()
            val body = response.body() ?: return
            sessionId = body.chatData.queryId
            addChatData(body.chatData)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    suspend fun nextChat(message: String) {
        TTS.stopSpeaking()
        if (message.isBlank()) return
        history.lastOrNull()?.let { if (it.speaker == Speaker.OPTIONS) history.removeLast() }
        history.add(Message(Speaker.USER, message))
        withContext(Dispatchers.Main) { listState.scrollToItem(history.lastIndex) }
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
            val body = response.body() ?: return
            addChatData(body.chatData)
        } catch (e: Exception) {
            handleException(e)
        } finally {
            chatIoState.value = false
        }
    }

    suspend fun getRelate(queryRecordItemId: String) {
        val response = Client.apiService.relateQuestion(queryRecordItemId).execute()
        val body = response.body() ?: return
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
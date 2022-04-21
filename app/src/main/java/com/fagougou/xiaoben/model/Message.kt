package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

enum class Speaker { ROBOT, USER }
data class Message(val speaker: Speaker, val content:String, val recommendQA: List<String> = listOf())

data class StartChatResult(
    @SerializedName("data")
    val startChatData:StartChatData = StartChatData()
)

data class StartChatData(
    @SerializedName("botSays")
    val botSays: List<BotSay> = listOf()
)

data class BotSay(
    @SerializedName("content")
    val content:BotSaysContent = BotSaysContent(),

    @SerializedName("recommendQA")
    val recommendQA:List<String> = listOf()
)

data class BotSaysContent(
    @SerializedName("body")
    val body:String = ""
)

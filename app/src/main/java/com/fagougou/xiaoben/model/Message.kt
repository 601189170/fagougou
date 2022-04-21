package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

enum class Speaker { ROBOT, USER, RECOMMEND, LAW, OPTIONS }

data class Message(
    val speaker: Speaker,
    val content:String = "",
    val recommends: List<String> = listOf(),
    val laws: List<Law> = listOf(),
    val option: Option = Option()
)

data class ChatRequest(
    @SerializedName("message")
    val message:String = ""
)

data class ChatResponse(
    @SerializedName("data")
    val chatData:ChatData = ChatData()
)

data class ChatData(

    @SerializedName("queryId")
    val queryId: String = "",

    @SerializedName("botSays")
    val botSays: List<BotSay> = listOf(),

    @SerializedName("option")
    val option: Option = Option(),
)

data class BotSay(
    @SerializedName("type")
    val type:String = "",

    @SerializedName("content")
    val content:BotSaysContent = BotSaysContent(),

    @SerializedName("recommendQA")
    val recommends:List<String> = listOf(),

    @SerializedName("isAnswered")
    val isAnswered:Boolean = false,
)

data class BotSaysContent(
    @SerializedName("title")
    val title:String = "",

    @SerializedName("body")
    val body:String = "",

    @SerializedName("laws")
    val laws:List<Law> = listOf(),

    @SerializedName("queryRecordItemId")
    val queryRecordItemId:String = "",

    @SerializedName("description")
    val description:String = "",

    @SerializedName("url")
    val url:String = "",
)

data class Law(
    @SerializedName("law")
    val name:String = "",
    @SerializedName("tiaoMu")
    val position:String = "",
    @SerializedName("content")
    val content:String = "",
)

data class Option(
    @SerializedName("type")
    val type:String = "",

    @SerializedName("title")
    val title:String = "",

    @SerializedName("subTitle")
    val subTitle:String = "",

    @SerializedName("items")
    val items:List<String> = listOf(),
)

data class RelateResponse(
    @SerializedName("data")
    val data:RelateData = RelateData(),
)

data class RelateData(
    @SerializedName("botSays")
    val says:List<RelateBotSays> = listOf(),
)

data class RelateBotSays(
    @SerializedName("content")
    val content:RelateContent = RelateContent()
)

data class RelateContent(
    @SerializedName("option")
    val questions:List<String> = listOf()
)
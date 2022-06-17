package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

enum class Speaker { ROBOT, USER, RECOMMEND, OPTIONS, COMPLEX }

data class Message(
    val speaker: Speaker = Speaker.USER,
    val content:String = "",
    val inlineRecommend:List<String> = listOf(),
    val recommends: List<String> = listOf(),
    val laws: List<Law> = listOf(),
    val option: Option = Option(),
    val complex: BotSaysContent = BotSaysContent(),
    var isExpend:Boolean = false,
    var listDef:List<ContentStyle> = listOf()
)

data class ChatRequest(
    @SerializedName("message")
    val message:String = ""
)

data class ChatResponse(
    val status:String = "",
    val message: String = "",
    @SerializedName("data")
    val chatData:ChatData = ChatData()
)

data class ChatData(
    val queryId: String = "",
    val botSays: List<BotSay> = listOf(),
    val option: Option = Option(),
)

data class BotSay(
    val type:String = "",
    val content:BotSaysContent = BotSaysContent(),
    @SerializedName("recommendQA")
    val recommends:List<String> = listOf(),
    val isAnswered:Boolean = false,
)

data class BotSaysContent(
    val title:String = "",
    val body:String = "",
    val laws:List<Law> = listOf(),
    val queryRecordItemId:String = "",
    val description:String = "",
    val url:String = "",
    val attachmentId:String = "",
    val explanation:String = "",
)

data class Law(
    @SerializedName("law")
    val name:String = "",
    @SerializedName("tiaoMu")
    val position:String = "",
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
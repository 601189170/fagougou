package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

data class BotList(
    @SerializedName("data")
    val data:List<Bot> = listOf()
)

data class Bot(
    @SerializedName("_id")
    val id:String = "",
    @SerializedName("name")
    val name:String = "",
    @SerializedName("tyTalkTopicId")
    val tyId:String = "",
)

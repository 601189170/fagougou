package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

data class DefineRequest (
//{"word":"#def::夫妻共同财产#"}
    @SerializedName("word")
    val word:String="",
)
data class DefineResponse (
    @SerializedName("data")
    val data:String="",
)

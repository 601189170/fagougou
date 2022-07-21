package com.fagougou.government.model

import com.google.gson.annotations.SerializedName


data class ContractUrlBean(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data
)

data class Data(
    @SerializedName("url")
    val url: String
)


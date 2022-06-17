package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

data class SerialRegisterRequest(
    val serial: String = "",
    val register: String ="",
    val machineType: String = "一体机"
)

data class SerialRegisterResponse(
    val balance: Int = -1,
    val errorMessage: String = ""
)

data class SerialLoginRequest(
    val serial: String = "",
)

data class SerialLoginResponse(
    val canLogin: Boolean = false,
    val errorMessage: String = "",
    @SerializedName("appid")
    val appId:String = "",
    @SerializedName("appsec")
    val appSec:String = "",
    val mkt:String = ""
)



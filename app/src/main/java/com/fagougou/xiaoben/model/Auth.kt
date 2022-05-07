package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("appid")
    val appId: String = "fgg10819e3417bf1a8",

    @SerializedName("appsec")
    val appSec: String = "175646a001a211e897e52593417bf1a8",
)

data class Auth(
    @SerializedName("data")
    val data: AuthData = AuthData(),
)

data class AuthData(
    @SerializedName("accessToken")
    val token:String = "",
)

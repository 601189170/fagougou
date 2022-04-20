package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("appid")
    val appId: String = "fggcc163120c487778",

    @SerializedName("appsec")
    val appSec: String = "cc163152002111ec90984d720c487778",
)

data class Auth(
    @SerializedName("data")
    val data: AuthData = AuthData(),
)

data class AuthData(
    @SerializedName("accessToken")
    val token:String = "",
)

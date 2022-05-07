package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("appid")
    val appId: String = "fgge004fd165560146",

    @SerializedName("appsec")
    val appSec: String = "ee241a20fb2311e790558d6165560146",
)

data class Auth(
    @SerializedName("data")
    val data: AuthData = AuthData(),
)

data class AuthData(
    @SerializedName("accessToken")
    val token:String = "",
)

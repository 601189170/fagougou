package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
//    @SerializedName("appid")
//    val appId: String = "fgg10819e3417bf1a8",
//
//    @SerializedName("appsec")
//    val appSec: String = "175646a001a211e897e52593417bf1a8",

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

package com.fagougou.government.model

import com.fagougou.government.utils.MMKV
import com.fagougou.government.utils.MMKV.kv
import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("appid")
    val appId: String = kv.decodeString(MMKV.appId,"")?:"",

    @SerializedName("appsec")
    val appSec: String = kv.decodeString(MMKV.appSec,"")?:"",
)

data class Auth(
    @SerializedName("data")
    val data: AuthData = AuthData(),
)

data class AuthData(
    @SerializedName("accessToken")
    val token:String = "",
)

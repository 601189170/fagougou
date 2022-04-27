package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("username")
    val username: String = "",
    @SerializedName("password")
    val password: String = "",
)

data class UserResult(
    @SerializedName("_id")
    val id: String = "",
)

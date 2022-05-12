package com.fagougou.government.model

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
    @SerializedName("channels")
    val channels: List<UserChannel> = listOf(),
)

data class UserChannel(
    @SerializedName("url")
    val url: String = "",
)

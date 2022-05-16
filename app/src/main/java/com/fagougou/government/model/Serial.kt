package com.fagougou.government.model

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
    val errorMessage: String = ""
)

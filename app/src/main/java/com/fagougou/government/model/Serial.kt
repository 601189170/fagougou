package com.fagougou.government.model

data class SerialRegiterRequest(
    val serial: String = "",
    val register: String ="",
    val machineType: String = "一体机"
)

data class SerialRegisterRespon(
    val balance: Int = -1,
    val errorMessage: String = ""
)

data class SerialLoginRequest(
    val serial: String = "",
)

data class SerialLoginRespon(
    val canlogin: Boolean = false,
    val errorMessage: String = ""
)

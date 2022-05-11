package com.fagougou.xiaoben.model

data class SerialRegiterRequest(
    val serial: String = "",
    val register: String =""
)

data class SerialRegisterRespon(
    val balance: String = "",
    val errorMessage: String = ""
)

data class SerialLoginRequest(
    val serial: String = "",
)

data class SerialLoginRespon(
    val canlogin: Boolean = false,
    val errorMessage: String = ""
)

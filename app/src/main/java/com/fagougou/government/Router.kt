package com.fagougou.government

import androidx.compose.runtime.mutableStateOf


object Router {
    const val touchWaitTime = 150L*1000L
    const val showTimeoutDialog = 30L*1000L

    const val login = "login"
    const val register = "register"
    const val registerResult = "registerResult"
    const val home = "home"
    const val admin = "admin"
    const val contract ="contract"
    const val contractWebView ="contractWebView"
    const val generateGuide = "generateGuide"
    const val generateContract = "generateContract"
    const val chatGuide ="chatGuide"
    const val chat ="chat"
    const val complex ="complex"
    const val case ="case"
    const val statistic ="statistic"
    const val calculator ="calculator"
    const val webView ="webView"
    const val about ="about"
    const val settings ="settings"
    const val reportMain ="reportMain"
    const val casePage ="casePage"
    const val lawsPage ="lawsPage"
    const val reportPage ="reportPage"
    const val upload ="upload"
    const val scanUpload ="scanUpload"
    const val resultWebview ="resultWebview"
    const val previewLoad ="previewLoad"
    const val lawyer ="lawyer"
    const val self ="self"
    const val examination ="examination"
    const val contractSelectPage ="contractSelectPage"

    var routeMirror = ""
    var lastTouchTime = Long.MAX_VALUE

    var routeRemain = mutableStateOf(0L)
    val noAutoQuitList = listOf(
        register,
        registerResult,
        home
    )
    val noLoadingPages = listOf(
        register,
        home,
        chat
    )

}
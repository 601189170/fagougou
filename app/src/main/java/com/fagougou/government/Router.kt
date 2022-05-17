package com.fagougou.government


object Router {
    const val login = "login"
    const val registerResult = "registerResult"
    const val home ="home"
    const val contract ="contract"
    const val generateGuide = "generateGuide"
    const val generateContract = "generateContract"
    const val chatGuide ="chatGuide"
    const val chat ="chat"
    const val complex ="complex"
    const val case ="case"
    const val statistic ="statistic"
    const val calculator ="calculator"
    const val contractWebView ="contractWebView"
    const val webView ="webView"
    const val about ="about"

    var routeMirror = ""
    val noLoadingPages = listOf(
        login,
        chat
    )
}
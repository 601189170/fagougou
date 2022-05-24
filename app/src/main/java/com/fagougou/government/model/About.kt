package com.fagougou.government.model

data class About(
    val data: AboutData = AboutData(),
)

data class AboutData(
    val firm: AboutFirm = AboutFirm(),
)

data class AboutFirm(
    val allNumber: Int = 0,
    val monthNumber: Int = 0,
    val qrcodeUrl: String = "",
)

package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class About(
    @SerializedName("data")
    val aboutData: AboutData = AboutData(),
)

data class AboutData(
    @SerializedName("firm")
    val aboutFirm: AboutFirm = AboutFirm(),
)

data class AboutFirm(
    @SerializedName("allNumber")
    val allNumber: Int = 0,

    @SerializedName("monthNumber")
    val monthNumber: Int = 0,

    @SerializedName("qrcodeUrl")
    val qrcodeUrl: String = "",
)

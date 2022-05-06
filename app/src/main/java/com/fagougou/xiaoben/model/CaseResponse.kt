package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class CaseResponse(
    @SerializedName("data")
    val data: CaseData = CaseData(),
)

data class CaseData(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("审判程序")
    val 审判程序: String = "",
    @SerializedName("案件名称")
    val 案件名称: String = "",
    @SerializedName("法院名称")
    val 法院名称: String = "",
    @SerializedName("裁判日期")
    val 裁判日期: String = "",
    @SerializedName("DocContent")
    var DocContent: String = "",
)

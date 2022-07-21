package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

data class UpLoadBean2(
    @SerializedName("title")
    val title: String="测试文件",
    @SerializedName("categoryId")

    val categoryId: String="234f2ed0-f1d6-11ec-ae28-bd7b6644f76f",
    @SerializedName("rulesType")

    val rulesType: String="系统推荐",
    @SerializedName("rulesTypeId")

    val rulesTypeId: String="234f2ed0-f1d6-11ec-ae28-bd7b6644f76f",
    @SerializedName("ownerId")

    val ownerId: String="61834b0368eb700034efef7a",
    @SerializedName("category")

    val category: String="通用合同（新）",
    @SerializedName("fileUrl")

    val fileUrl: String="https://temp-1251511189.cos.ap-guangzhou.myqcloud.com/1658210619431_957807.docx"
)
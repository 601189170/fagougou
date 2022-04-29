package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("data")
    val attachmentData:AttachmentData = AttachmentData()
)

data class AttachmentData(
    @SerializedName("title")
    val title:String = "",
    @SerializedName("content")
    val content:AttachmentContent = AttachmentContent(),
    @SerializedName("type")
    val type:String = "",
)

data class AttachmentContent(
    @SerializedName("chart")
    val chart:AttachmentChart = AttachmentChart(),
    @SerializedName("cases")
    val cases:List<AttachmentCases> = listOf(),
    @SerializedName("body")
    val body:List<AttachmentBody> = listOf()
)

data class AttachmentChart(
    @SerializedName("title")
    val title:String = "",
    @SerializedName("subtitle")
    val subtitle:String = "",
    @SerializedName("lableName")
    val lable:String = "",
    @SerializedName("type")
    val type:String = "",
    @SerializedName("data")
    val data:List<AttachmentChartData> = listOf()
)

data class AttachmentChartData(
    @SerializedName("name")
    val name:String = "",
    @SerializedName("value")
    val value:Float = 0f,
)

data class AttachmentCases(
    @SerializedName("judgmentDate")
    val date:String = "",
    @SerializedName("courtName")
    val courtName:String = "",
    @SerializedName("caseName")
    val caseName:String = "",
    @SerializedName("caseNumber")
    val caseNumber:String = "",
)

data class AttachmentLaws(
    @SerializedName("law")
    val name:String = "",
    @SerializedName("tiaoMu")
    val position:String = "",
    @SerializedName("content")
    val content:String = "",
)

data class AttachmentBody(
    @SerializedName("title")
    val title:String = "",
    @SerializedName("content")
    val content:String = "",
)

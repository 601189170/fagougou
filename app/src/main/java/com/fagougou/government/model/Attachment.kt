package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    val data:AttachmentData = AttachmentData()
)

data class AttachmentData(
    val title:String = "",
    val content:AttachmentContent = AttachmentContent(),
    val type:String = "",
)

data class AttachmentContent(
    val chart:AttachmentChart = AttachmentChart(),
    val cases:List<AttachmentCases> = listOf(),
    val body:List<AttachmentBody> = listOf(),
    val laws:List<AttachmentLaws> = listOf(),
    val contexts:List<String> = listOf(),
    val title:String = ""
)

data class AttachmentChart(
    val title:String = "",
    val subtitle:String = "",
    @SerializedName("lableName")
    val lable:String = "",
    val type:String = "",
    val data:List<AttachmentChartData> = listOf()
)

data class AttachmentChartData(
    val name:String = "",
    val value:Float = 0f,
)

data class AttachmentCases(
    @SerializedName("judgmentDate")
    val date:String = "",
    @SerializedName("courtName")
    val court:String = "",
    @SerializedName("caseName")
    val name:String = "",
    @SerializedName("caseNumber")
    val number:String = "",
    val serial:String = ""
)

data class AttachmentBody(
    val title:String = "",
    val content:String = "",
)

data class AttachmentLaws(
    @SerializedName("law")
    val law:String = "",
    @SerializedName("tiaoMu")
    val tiaoMu:String = "",
    @SerializedName("content")
    val content:String = "",
    @SerializedName("isExpan")
    val isExpan:Boolean = false,
)


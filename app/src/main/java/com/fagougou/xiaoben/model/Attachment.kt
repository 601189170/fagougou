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
    val content:AttachmentContent = AttachmentContent()
)

data class AttachmentContent(
    @SerializedName("title")
    val title:String = "",
    @SerializedName("content")
    val content:AttachmentData = AttachmentData()
)

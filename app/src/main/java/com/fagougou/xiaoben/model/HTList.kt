package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class HTListRequest(
    @SerializedName("folder")
    val folder: String = "",
    @SerializedName("limit")
    val limit: String = "20",
)

data class HTList(
    val `data`: Data = Data(),
)

data class Data(
    val count: Int = 0,
    val list: List<DataB> = listOf(),
    val query: Query = Query()
)

data class DataB(
    val fileid: String = "",
    val folder: Folder = Folder(),
    val howToUse: String = "",
    val name: String = "",
    val updatedAt: String = ""
)

data class Query(
    val folder: String = "",
    val folderName: String = "",
    val limit: Int = 20,
    val name: String = "",
    val skip: Int = 0
)

data class Folder(
    val name: String = ""
)
package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class HTList(
    val `data`: Data,
    val errCode: Int
) {
    data class Data(
        val count: Int,
        val list: List<DataB>,
        val query: Query
    )

    data class DataB(
        val __v: Int,
        val _id: String,
        val createdAt: String,
        val fileid: String,
        val folder: Folder,
        val howToUse: String,
        val id: String,
        val name: String,
        val offline: Boolean,
        val updatedAt: String
    )

    data class Query(
        val folder: String,
        val folderName: Any,
        val limit: Int,
        val name: String,
        val skip: Int
    )

    data class Folder(
        val _id: String,
        val id: String,
        val name: String
    )
}
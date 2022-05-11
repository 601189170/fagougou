package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class ContractListRequest(
    @SerializedName("folder")
    val folder: String = "",
    @SerializedName("limit")
    val limit: String = "20",
    @SerializedName("name")
    val name: String = "",
)

data class ContractList(
    val `data`: ContractListData = ContractListData(),
)

data class ContractListData(
    val count: Int = 0,
    val list: List<ContractData> = listOf(),
    val query: ContractQuery = ContractQuery()
)

data class ContractData(
    val fileid: String = "",
    val folder: ContractFolder? = ContractFolder(),
    val howToUse: String? = "",
    val name: String = "",
)

data class ContractQuery(
    val folder: String = "",
    val folderName: String = "",
    val limit: Int = 20,
    val name: String = "",
    val skip: Int = 0
)

data class ContractFolder(
    val name: String = ""
)
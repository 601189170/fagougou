package com.fagougou.government.model

data class ContractListRequest(
    val folder: String = "",
    val limit: String = "20",
    val name: String = "",
)

data class ContractList(
    val data: ContractListData = ContractListData(),
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
    val _id: String = "",
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

data class TemplateBean(
    val code: Int,
    val `data`: String
)


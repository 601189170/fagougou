package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

data class ContractCategoryResponse(
    @SerializedName("data")
    val categorys: List<ContractCategory> = listOf(),
)

data class ContractCategory(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("children")
    val children: List<ContractCategory>,
    @SerializedName("isRoot")
    val isRoot: String = "",
    @SerializedName("parentId")
    val parentId: String = "",
)
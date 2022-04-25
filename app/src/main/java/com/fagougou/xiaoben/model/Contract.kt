package com.fagougou.xiaoben.model

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
)
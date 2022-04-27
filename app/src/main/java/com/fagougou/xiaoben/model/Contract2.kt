package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class ContractCategoryResponse2(
    val `data`: List<Data>,
    val errCode: Int
)
{
data class Data(
    val _id: String,
    val children: List<Data>,
    val id: String,
    val isRoot: Boolean,
    val name: String,
    val parentId: String?=""

)



}
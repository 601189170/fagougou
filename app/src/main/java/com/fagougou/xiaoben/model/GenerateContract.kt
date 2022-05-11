package com.fagougou.xiaoben.model

import com.google.gson.annotations.SerializedName

data class GenerateListRequest(
    val folder: String = "60dc8f3bdd76000013807315",
    val page: Int = 1,
    val size: Int = 10,
)

data class GenerateContractListResponse(
    val data:GenerateContractList = GenerateContractList()
)

data class GenerateContractList(
    val list:List<GenerateContractBrief> = listOf()
)

data class GenerateContractBrief(
    val name:String = "",
    @SerializedName("_id")
    val id:String = "",
    val howToUse:String = ""
)

data class GenerateContractTempleteResponse(
    val data:GenerateContractTemplete = GenerateContractTemplete()
)

data class GenerateContractTemplete(
    val content:String = ""
)

data class GenerateContractResponse(
    val data:GenerateData = GenerateData()
)

data class GenerateData(
    val forms:List<GenerateForm> = listOf()
)

data class GenerateForm(
    val label:String = "",
    val variable:String = "",
    var children:MutableList<GenerateFormChild> = mutableListOf()
)

data class GenerateFormChild(
    val label: String = "",
    val variable: String = "",
    val default:String = "",
    val type:String = "",
    val comment:String = "",
    var value:String = ""
)
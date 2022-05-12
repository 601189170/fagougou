package com.fagougou.government.model

import com.google.gson.annotations.SerializedName

data class GenerateListRequest(
    val folder: String = "60dc8f10dd7600001380730b",
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
    val values:List<String> = listOf(),
    var input:String = "",
    var selected:MutableSet<Int> = mutableSetOf()
)
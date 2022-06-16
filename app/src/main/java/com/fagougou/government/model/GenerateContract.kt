package com.fagougou.government.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class GenerateContractListResponse(
    val data:List<GenerateContractBrief> = listOf()
)

data class GenerateContractBrief(
    val name:String = "",
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
    var input: MutableState<String> = mutableStateOf(""),
    var selected:SnapshotStateList<Int> = mutableStateListOf()
)
package com.fagougou.government.model

class PdfFile(
    val id:String,
    val name:String,
    val updateAt:String
) {
    constructor( category: ContractData ) : this(category.id,category.name,category.updatedAt)
}
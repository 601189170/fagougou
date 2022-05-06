package com.fagougou.xiaoben.utils

import java.lang.StringBuilder

object InlineRecommend {
    val questionRegex = Regex("#question::(.*?)#")
    val defRegex = Regex("#def::.*?#")

    fun String.removeInline():String{
        return replace(defRegex,"").replace(questionRegex,"").replace("\n","")
    }

    fun String.getInline():List<String>{
        var isQuestion = false
        val inputList = split("#")
        val builder = StringBuilder()
        val resultList = mutableListOf<String>()
        for(word in inputList){
            when {
                isQuestion -> {
                    if (!word.contains("def::")){
                        builder.append(word)
                        resultList.add(builder.toString())
                        builder.clear()
                        isQuestion = false
                    }else builder.append(word.replace("def::",""))
                }
                word=="question::" -> {
                    isQuestion = true
                }
                word.contains("question::") -> {
                    resultList.add(word.replace("question::",""))
                }
            }
        }
        return resultList
    }
}
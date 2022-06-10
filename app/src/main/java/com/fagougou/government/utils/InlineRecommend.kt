package com.fagougou.government.utils

import android.text.TextUtils
import com.fagougou.government.model.ContentStyle

object InlineRecommend {
    val questionRegex = Regex("[\n]*#question::")
    val defRegex = Regex("[\n]*#def::")
    val sharpRegex = Regex("#[\n]*")

    fun String.removeInline():String{
        return replace(defRegex,"")
            .replace(questionRegex,"")
            .replace(sharpRegex,"")
            .replace("%||%","\n")
    }
    fun String.removeData():String{
        return replace(questionRegex,"")
            .replace("%||%","\n")
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

    var def="#def::"
    var regex="#"

    fun getDefData(content: String):List<ContentStyle> {
        val resultList = mutableListOf<Int>()
        val subContent = mutableListOf<ContentStyle>()
        val max=content.length
        for (i in content.indices){
            if (content[i].toString() == regex){
                resultList.add(i)
            }
        }

        for (i in resultList.indices){
            if (i==0){

                val startData=content.substring(0, resultList[0])
                if (!TextUtils.isEmpty(startData)){
                    subContent.add(ContentStyle(startData,0))
                }
            }else{
                val subData=content.substring(resultList[i-1], resultList[i])
                if (!TextUtils.isEmpty(subData)){
                    if (subData.contains(def)){
                        val data=subData.replace(def,"").replace(regex,"")
                        if (!TextUtils.isEmpty(data)) subContent.add(ContentStyle(data,1))
                    }else{
                        val data=subData.replace(def,"").replace(regex,"")
                        if (!TextUtils.isEmpty(data)) subContent.add(ContentStyle(data,0))
                    }
                }
            }
            if (i==resultList.size-1){
                val lastData=content.substring(resultList[i],max).replace(regex,"")
                if (!TextUtils.isEmpty(lastData)) subContent.add(ContentStyle(lastData,0))
            }
        }
        return subContent
    }
}
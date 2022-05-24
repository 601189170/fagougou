package com.fagougou.government.utils

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
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
    var regx="#";

    fun getDefData(content: String):List<ContentStyle> {
        val resultList = mutableListOf<Int>()
        val subContent = mutableListOf<ContentStyle>()
        var max=content.length
        for (i in content.indices){
            if (content.get(i).toString().equals(regx)){
                resultList.add(i)
            }
        }
        Log.e("TAG", "resultList: "+ JSON.toJSONString(resultList))

        for (i in resultList.indices){
            if (i==0){

                var startdata=content.substring(0,resultList.get(0))
                if (!TextUtils.isEmpty(startdata)){
                    subContent.add(ContentStyle(startdata,0))
                }
            }else{
                var subdata=content.substring(resultList.get(i-1),resultList.get(i))
                if (!TextUtils.isEmpty(subdata)){
                    if (subdata.contains(def)){
                        var data=subdata.replace(def,"").replace(regx,"")
                        if (!TextUtils.isEmpty(data))
                            subContent.add(ContentStyle(data,1))
                    }else{
                        var data=subdata.replace(def,"").replace(regx,"")
                        if (!TextUtils.isEmpty(data))
                            subContent.add(ContentStyle(data,0))
                    }
                }
            }
            if (i==resultList.size-1){
                var lastdata=content.substring(resultList.get(i),max).replace(regx,"")
                if (!TextUtils.isEmpty(lastdata)){
                    subContent.add(ContentStyle(lastdata,0))
                }
            }
        }

        Log.e("TAG", "subContent: "+ JSON.toJSONString(subContent))
        return subContent
    }


}
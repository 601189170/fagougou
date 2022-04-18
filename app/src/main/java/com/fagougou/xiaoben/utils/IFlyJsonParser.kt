package com.fagougou.xiaoben.utils

import org.json.JSONTokener
import org.json.JSONObject
import org.json.JSONArray
import java.lang.Exception

/**
 * Json结果解析类
 */
object IFlyJsonParser {
    fun parseIatResult(json: String?): String {
        val ret = StringBuffer()
        try {
            val tokener = JSONTokener(json)
            val joResult = JSONObject(tokener)
            val words = joResult.getJSONArray("ws")
            for (i in 0 until words.length()) {
                // 转写结果词，默认使用第一个结果
                val items = words.getJSONObject(i).getJSONArray("cw")
                val obj = items.getJSONObject(0)
                ret.append(obj.getString("w"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret.toString()
    }

    fun parseGrammarResult(json: String?, engType: String): String {
        val ret = StringBuffer()
        try {
            val tokener = JSONTokener(json)
            val joResult = JSONObject(tokener)
            val words = joResult.getJSONArray("ws")
            // 云端和本地结果分情况解析
            if ("cloud" == engType) {
                for (i in 0 until words.length()) {
                    val items = words.getJSONObject(i).getJSONArray("cw")
                    for (j in 0 until items.length()) {
                        val obj = items.getJSONObject(j)
                        if (obj.getString("w").contains("nomatch")) {
                            ret.append("没有匹配结果.")
                            return ret.toString()
                        }
                        ret.append("【结果】" + obj.getString("w"))
                        ret.append("【置信度】" + obj.getInt("sc"))
                        ret.append("\n")
                    }
                }
            } else if ("local" == engType) {
                ret.append("【结果】")
                for (i in 0 until words.length()) {
                    val wsItem = words.getJSONObject(i)
                    val items = wsItem.getJSONArray("cw")
                    if ("<contact>" == wsItem.getString("slot")) {
                        // 可能会有多个联系人供选择，用中括号括起来，这些候选项具有相同的置信度
                        ret.append("【")
                        for (j in 0 until items.length()) {
                            val obj = items.getJSONObject(j)
                            if (obj.getString("w").contains("nomatch")) {
                                ret.append("没有匹配结果.")
                                return ret.toString()
                            }
                            ret.append(obj.getString("w")).append("|")
                        }
                        ret.setCharAt(ret.length - 1, '】')
                    } else {
                        //本地多候选按照置信度高低排序，一般选取第一个结果即可
                        val obj = items.getJSONObject(0)
                        if (obj.getString("w").contains("nomatch")) {
                            ret.append("没有匹配结果.")
                            return ret.toString()
                        }
                        ret.append(obj.getString("w"))
                    }
                }
                ret.append("【置信度】" + joResult.getInt("sc"))
                ret.append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ret.append("没有匹配结果.")
        }
        return ret.toString()
    }

    fun parseGrammarResult(json: String?): String {
        val ret = StringBuffer()
        try {
            val tokener = JSONTokener(json)
            val joResult = JSONObject(tokener)
            val words = joResult.getJSONArray("ws")
            for (i in 0 until words.length()) {
                val items = words.getJSONObject(i).getJSONArray("cw")
                for (j in 0 until items.length()) {
                    val obj = items.getJSONObject(j)
                    if (obj.getString("w").contains("nomatch")) {
                        ret.append("没有匹配结果.")
                        return ret.toString()
                    }
                    ret.append("【结果】" + obj.getString("w"))
                    ret.append("【置信度】" + obj.getInt("sc"))
                    ret.append("\n")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ret.append("没有匹配结果.")
        }
        return ret.toString()
    }

    fun parseLocalGrammarResult(json: String?): String {
        val ret = StringBuffer()
        try {
            val tokener = JSONTokener(json)
            val joResult = JSONObject(tokener)
            val words = joResult.getJSONArray("ws")
            for (i in 0 until words.length()) {
                val items = words.getJSONObject(i).getJSONArray("cw")
                for (j in 0 until items.length()) {
                    val obj = items.getJSONObject(j)
                    if (obj.getString("w").contains("nomatch")) {
                        ret.append("没有匹配结果.")
                        return ret.toString()
                    }
                    ret.append("【结果】" + obj.getString("w"))
                    ret.append("\n")
                }
            }
            ret.append("【置信度】" + joResult.optInt("sc"))
        } catch (e: Exception) {
            e.printStackTrace()
            ret.append("没有匹配结果.")
        }
        return ret.toString()
    }

    fun parseTransResult(json: String?, key: String?): String {
        val ret = StringBuffer()
        try {
            val tokener = JSONTokener(json)
            val joResult = JSONObject(tokener)
            val errorCode = joResult.optString("ret")
            if (errorCode != "0") {
                return joResult.optString("errmsg")
            }
            val transResult = joResult.optJSONObject("trans_result")
            ret.append(transResult.optString(key))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret.toString()
    }
}
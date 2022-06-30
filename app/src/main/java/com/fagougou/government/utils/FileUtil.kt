package com.fagougou.government.utils

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

object FileUtil {
    val FilePath=Environment.getExternalStorageDirectory().absolutePath+"/"+System.currentTimeMillis()/1000+"web.html"

     fun autoToHTML( path: String, templateString: String) {
        //用于存储html字符串
        val stringHtml = StringBuilder()
        try {
            val printStream = PrintStream(FileOutputStream(path))
            //输入HTML文件内容
            stringHtml.append(templateString)

            //将HTML文件内容写入文件中
            printStream.println(stringHtml.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
package com.fagougou.government.utils

import Decoder.BASE64Decoder
import android.os.Environment
import java.io.*

object  FileUtils {
    val FILE_TO = Environment.getExternalStorageDirectory().path + File.separator + "ding.pdf"

    // InputStream -> File
    @Throws(IOException::class)
     fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        FileOutputStream(file).use { outputStream ->
            var read: Int
            val bytes = ByteArray(1024)
            while (inputStream.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        }
    }

    /**
     * 将base64编码转换成PDF，保存到
     * @param base64sString
     * 1.使用BASE64Decoder对编码的字符串解码成字节数组
     * 2.使用底层输入流ByteArrayInputStream对象从字节数组中获取数据；
     * 3.建立从底层输入流中读取数据的BufferedInputStream缓冲输出流对象；
     * 4.使用BufferedOutputStream和FileOutputSteam输出数据到指定的文件中
     */
    fun base64StringToPDF(base64sString: String?, filePath: String?) {
        val decoder = BASE64Decoder()
        var bin: BufferedInputStream? = null
        var fout: FileOutputStream? = null
        var bout: BufferedOutputStream? = null
        try {
            //将base64编码的字符串解码成字节数组
            val bytes = decoder.decodeBuffer(base64sString)
            //apache公司的API
            //byte[] bytes = Base64.decodeBase64(base64sString);
            //创建一个将bytes作为其缓冲区的ByteArrayInputStream对象
            val bais = ByteArrayInputStream(bytes)
            //创建从底层输入流中读取数据的缓冲输入流对象
            bin = BufferedInputStream(bais)
            //指定输出的文件
            val file = File(filePath)
            //创建到指定文件的输出流
            fout = FileOutputStream(file)
            //为文件输出流对接缓冲输出流对象
            bout = BufferedOutputStream(fout)
            val buffers = ByteArray(1024)
            var len = bin.read(buffers)
            while (len != -1) {
                bout.write(buffers, 0, len)
                len = bin.read(buffers)
            }
            //刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
            bout.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bin!!.close()
                fout!!.close()
                bout!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
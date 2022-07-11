package com.fagougou.government.utils

import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter

import java.io.File
import java.io.FileOutputStream

object PdfUtils {
    /**
     * @param imgPaths          图片地址
     * @param pdf_save_address  pdf保存地址
     */
    fun imageToPDF(imgPaths: String, pdf_save_address: File) {
        try {
            val document = Document()
            // 创建PdfWriter对象
            PdfWriter.getInstance(document, FileOutputStream(pdf_save_address))
            document.open()
            val img = Image.getInstance(imgPaths)
            val scale = (document.pageSize.width - document.leftMargin()
                    - document.rightMargin() - 0) / img.width * 100
            img.scalePercent(scale)
            img.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP
            document.add(img)
            document.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
//
//    @Throws(java.lang.Exception::class)
//    fun MergePdf(listfile:List<String>,path: String) {
//        //pdf合并工具类
//        val mergePdf = PDFMergerUtility()
//
//        listfile.forEach(){
//            mergePdf.addSource(it)
//        }
//        //设置合并生成pdf文件名称
//        mergePdf.destinationFileName = path
//        //合并pdf
//        mergePdf.mergeDocuments()
//    }
}
package com.fagougou.government.contractLibraryPage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PdfPrintAdapter(private val context: Context, private val pdfFile: File) :
    PrintDocumentAdapter() {
    private var pageHeight = 0
    private var pageWidth = 0
    private lateinit var mPdfDocument: PdfDocument
    private var totalpages = 1
    private var bitmapList = mutableListOf<Bitmap>()

    override fun onLayout(
        oldAttributes: PrintAttributes,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback: LayoutResultCallback,
        metadata: Bundle
    ) {
        mPdfDocument = PrintedPdfDocument(context, newAttributes) //创建可打印PDF文档对象
        pageHeight = PrintAttributes.MediaSize.ISO_A4.heightMils * 72 / 1000 //设置尺寸
        pageWidth = PrintAttributes.MediaSize.ISO_A4.widthMils * 72 / 1000
        if (cancellationSignal.isCanceled) {
            callback.onLayoutCancelled()
            return
        }
        try {
            val mFileDescriptor =
                ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRender = PdfRenderer(mFileDescriptor)
            if (pdfRender.pageCount > 0) {
                totalpages = pdfRender.pageCount
                for (i in 0 until pdfRender.pageCount) {
                    val page = pdfRender.openPage(i)
                    val bmp = Bitmap.createBitmap(
                        page.width * 2,
                        page.height * 2,
                        Bitmap.Config.ARGB_8888
                    )
                    page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmapList.add(bmp)
                    page.close()
                }
            }
            mFileDescriptor.close()
            pdfRender.close()
        } catch (e: Exception) {
            callback.onLayoutFailed(e.message)
        }
        if (totalpages > 0) {
            val info = PrintDocumentInfo.Builder("print.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(totalpages) //构建文档配置信息
                .build()
            callback.onLayoutFinished(info, true)
        } else {
            callback.onLayoutFailed("Page count is zero.")
        }
    }

    override fun onWrite(
        pageRanges: Array<PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback
    ) {
        for (i in 0 until totalpages) {
            if (pageInRange(pageRanges, i)) {
                val newPage = PdfDocument.PageInfo.Builder(
                    pageWidth,
                    pageHeight, i
                ).create()
                val page = mPdfDocument.startPage(newPage) //创建新页面
                if (cancellationSignal.isCanceled) {  //取消信号
                    callback.onWriteCancelled()
                    mPdfDocument.close()
                    return
                }
                drawPage(page, i) //将内容绘制到页面Canvas上
                mPdfDocument.finishPage(page)
            }
        }
        try {
            mPdfDocument.writeTo(FileOutputStream(destination.fileDescriptor))
            callback.onWriteFinished(pageRanges)
        } catch (e: IOException) {
            callback.onWriteFailed(e.message)
        } finally {
            mPdfDocument.close()
        }
    }

    private fun pageInRange(pageRanges: Array<PageRange>, page: Int): Boolean {
        for (pageRange in pageRanges) if (page >= pageRange.start && page <= pageRange.end) return true
        return false
    }

    //页面绘制（渲染）
    private fun drawPage(page: PdfDocument.Page, index: Int) {
        val canvas = page.canvas
        val paint = Paint()
        val bitmap = bitmapList[index]
        val bitmapWidth = bitmap.width
        // 计算缩放比例
        val scale = pageWidth.toFloat() / bitmapWidth.toFloat()
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        canvas.drawBitmap(bitmap, matrix, paint)
    }
}
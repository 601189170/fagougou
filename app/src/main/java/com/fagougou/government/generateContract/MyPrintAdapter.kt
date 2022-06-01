package com.fagougou.government.generateContract

import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.util.Log

class MyPrintAdapter : PrintDocumentAdapter() {
    override fun onLayout(
        p0: PrintAttributes?,
        p1: PrintAttributes?,
        p2: CancellationSignal?,
        p3: LayoutResultCallback?,
        p4: Bundle?
    ) {
        TODO("Not yet implemented")
    }

    override fun onWrite(
        p0: Array<out PageRange>?,
        p1: ParcelFileDescriptor?,
        p2: CancellationSignal?,
        p3: WriteResultCallback?
    ) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG", "onStart: 开始打印 " )
    }

    override fun onFinish() {
        super.onFinish()
        Log.e("TAG", "结束打印: ", )
    }
}
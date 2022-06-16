package com.fagougou.government

import android.content.Intent
import android.os.Process
import android.util.Log
import com.bugsnag.android.Bugsnag
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

class CrashHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Log.e(
            "程序出现异常了", """Thread = ${thread.name}Throwable = ${throwable.message}""".trimIndent())
        val stackTraceInfo = getStackTraceInfo(throwable)
        Log.e("程序出现异常了", stackTraceInfo)
        Bugsnag.notify(throwable as java.lang.Exception)
        //重启
        val intent = Intent(CommonApplication.activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        CommonApplication.activity.startActivity(intent)
        Process.killProcess(Process.myPid())
    }

    /**
     * 获取错误的信息
     * @param throwable
     * @return
     */
    private fun getStackTraceInfo(throwable: Throwable): String {
        var pw: PrintWriter? = null
        val writer: Writer = StringWriter()
        try {
            pw = PrintWriter(writer)
            throwable.printStackTrace(pw)
        } catch (e: Exception) {
            return ""
        } finally {
            pw?.close()
        }
        return writer.toString()
    }
}
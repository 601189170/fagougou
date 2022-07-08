package com.fagougou.government

import android.content.Intent
import android.os.Process
import com.bugsnag.android.Bugsnag
import timber.log.Timber

class CrashHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Timber.e(throwable)
        Bugsnag.notify(throwable as java.lang.Exception)
        //重启
        val intent = Intent(CommonApplication.activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        CommonApplication.activity.startActivity(intent)
        Process.killProcess(Process.myPid())
    }
}
package com.fagougou.government.utils

import android.app.ZysjSystemManager

object ZYSJ {

    var manager:ZysjSystemManager? = null

    fun showBar() = manager?.ZYSystemBar(1)

    fun hideBar() = manager?.ZYSystemBar(0)

    fun shutdown() = manager?.ZYShutdownSys()

    fun reboot() = manager?.ZYRebootSys()

    fun openAdb() = manager?.ZYsetAdbWiress(true)

}
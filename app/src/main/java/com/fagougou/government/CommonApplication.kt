package com.fagougou.government

import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import com.bugsnag.android.Bugsnag
import com.fagougou.government.chatPage.ChatViewModel.botQueryIdMap
import com.fagougou.government.generateContract.GenerateContractViewModel
import com.fagougou.government.model.Auth
import com.fagougou.government.model.AuthRequest
import com.fagougou.government.model.BotList
import com.fagougou.government.presentation.BannerPresentation
import com.fagougou.government.repo.Client.apiService
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.TTS
import com.iflytek.cloud.SpeechUtility
import com.moor.imkf.utils.YKFUtils
import com.tencent.mmkv.MMKV
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.*
import timber.log.Timber

class CommonApplication: Application(){
    companion object {
        lateinit var activity: ComponentActivity
        val serial = if(Build.VERSION.SDK_INT>25) Build.getSerial() else Build.SERIAL
        var presentation : BannerPresentation? = null
        var currentCode = Int.MAX_VALUE
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        currentCode = packageManager.getPackageInfo(packageName, 0).versionCode
        UMConfigure.preInit(this,"62a2f39388ccdf4b7e90908c","Fagougou")
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"")
        SpeechUtility.createUtility(this, "appid=59fad520")
        MMKV.initialize(this)
        Bugsnag.start(this)
        IFly.init(this)
        TTS.init(this)
        GenerateContractViewModel.init(this)
        YKFUtils.init(this)
        Timber.plant(Timber.DebugTree())
        val crashHandler = CrashHandler()
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)
        CoroutineScope(Dispatchers.Default).launch {
            if (!Settings.canDrawOverlays(this@CommonApplication)){
                while (!Settings.canDrawOverlays(this@CommonApplication)) delay(500)
                openSecondScreen()
            }else openSecondScreen()
        }
    }

    private suspend fun openSecondScreen() {
        withContext(Dispatchers.Main){
            val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val display = displayManager.displays.getOrNull(1) ?: return@withContext
            presentation = BannerPresentation(this@CommonApplication, display)
            val windowType = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            presentation?.window?.setType(windowType) ?: return@withContext
            presentation?.show()
        }
    }
}
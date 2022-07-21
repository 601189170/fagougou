package com.fagougou.government

import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import com.bugsnag.android.Bugsnag
import com.fagougou.government.generateContract.GenerateContractViewModel
import com.fagougou.government.presentation.BannerPresentation
import com.fagougou.government.utils.Frpc
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.ImSdkUtils.initUdeskSDK
import com.fagougou.government.utils.TTS
import com.iflytek.cloud.SpeechUtility
import com.tencent.mmkv.MMKV
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.*
import timber.log.Timber

class CommonApplication: Application(){
    companion object {
        lateinit var activity: ComponentActivity
        val serial = when {
            Build.VERSION.SDK_INT >= 28 -> "EJT8XS3DSY"
            Build.VERSION.SDK_INT >= 26 ->  Build.getSerial()
            Build.VERSION.SDK_INT == 25 -> Build.SERIAL
            else -> "EJT8XS3DSY"
        }
        var presentation : BannerPresentation? = null
        var currentCode = Int.MAX_VALUE

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        currentCode = packageManager.getPackageInfo(packageName, 0).versionCode
        Timber.plant(Timber.DebugTree())
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler())
        UMConfigure.preInit(this,"62a2f39388ccdf4b7e90908c","Fagougou")
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"")
        MobclickAgent.setDebugMode(true)
        SpeechUtility.createUtility(this, "appid=59fad520")
        MMKV.initialize(this)
        Bugsnag.start(this)
        IFly.init(this)
        TTS.init(this)
        GenerateContractViewModel.init(this)
        initUdeskSDK(this);
        initX5WebView()
        Frpc.connect(this@CommonApplication)
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

    private fun initX5WebView() {
        QbSdk.setDownloadWithoutWifi(true)
        /* SDK内核初始化周期回调，包括 下载、安装、加载 */
        QbSdk.setTbsListener(object : TbsListener {
            /**
             * @param stateCode 110: 表示当前服务器认为该环境下不需要下载
             */
            override fun onDownloadFinish(stateCode: Int) {}

            /**
             * @param stateCode 200、232安装成功
             */
            override fun onInstallFinish(stateCode: Int) {}

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            override fun onDownloadProgress(progress: Int) {}
        })
        var cb =  object : QbSdk.PreInitCallback{
            override fun onCoreInitFinished() {}
            override fun onViewInitFinished(p0: Boolean) { Timber.e("x5WebView==>"+p0) }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb)



    }


}
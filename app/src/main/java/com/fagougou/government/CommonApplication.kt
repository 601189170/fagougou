package com.fagougou.government

import android.app.Application
import android.app.ZysjSystemManager
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.bugsnag.android.Bugsnag
import com.fagougou.government.chatPage.ChatViewModel.botQueryIdMap
import com.fagougou.government.generateContract.GenerateContract
import com.fagougou.government.model.Auth
import com.fagougou.government.model.AuthRequest
import com.fagougou.government.model.BotList
import com.fagougou.government.presentation.BannerPresentation
import com.fagougou.government.repo.Client.apiService
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.TTS
import com.fagougou.government.utils.Tips
import com.iflytek.cloud.SpeechUtility
import com.moor.imkf.utils.YKFUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*

class CommonApplication: Application(){
    companion object {
        lateinit var activity: ComponentActivity
        const val TAG = "FaGouGou@XiaoBen"

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        Tips.init(this)
        SpeechUtility.createUtility(this, "appid=b9efca3f")
        MMKV.initialize(this)
        Bugsnag.start(this)
        IFly.init(this)
        TTS.init(this)
        GenerateContract.init(this)
        YKFUtils.init(this)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tokenResponse = apiService.auth(AuthRequest()).execute()
                val tokenBody = tokenResponse.body() ?: Auth()
                kv.encode("token", tokenBody.data.token)
                val botListResponse = apiService.botList().execute()
                val botListBody = botListResponse.body() ?: BotList()
                for (bot in botListBody.data) if (bot.tyId == "") botQueryIdMap[bot.name] = bot.id
            }catch (e:Exception){
                handleException(e)
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            if (!Settings.canDrawOverlays(this@CommonApplication)){
                while (!Settings.canDrawOverlays(this@CommonApplication)) delay(500)
                openSecondScreen()
            }else openSecondScreen()
        }
    }

    suspend fun openSecondScreen() {
        withContext(Dispatchers.Main){
            val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val display = displayManager.displays.getOrNull(1) ?: return@withContext
            val presentation = BannerPresentation(this@CommonApplication, display)
            val windowType = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            presentation.window?.setType(windowType) ?: return@withContext
            presentation.show()
        }
    }
}
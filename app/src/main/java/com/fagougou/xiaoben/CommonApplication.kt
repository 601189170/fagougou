package com.fagougou.xiaoben

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import com.bugsnag.android.Bugsnag
import com.fagougou.xiaoben.chatPage.ChatViewModel.botQueryIdMap
import com.fagougou.xiaoben.model.Auth
import com.fagougou.xiaoben.model.AuthRequest
import com.fagougou.xiaoben.model.BotList
import com.fagougou.xiaoben.repo.Client.apiService
import com.fagougou.xiaoben.repo.Client.handleException
import com.fagougou.xiaoben.utils.IFly
import com.fagougou.xiaoben.utils.MMKV.kv
import com.fagougou.xiaoben.utils.TTS
import com.iflytek.cloud.SpeechUtility
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

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
        SpeechUtility.createUtility(this, "appid=33b963d0")
        MMKV.initialize(this)
        Bugsnag.start(this)
//        IFly.init(this)
//        TTS.init(this)
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
    }
}
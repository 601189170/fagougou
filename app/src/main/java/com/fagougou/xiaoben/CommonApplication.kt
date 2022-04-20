package com.fagougou.xiaoben

import android.app.Application
import android.content.Context
import com.fagougou.xiaoben.chatPage.ChatPage.chatBotMap
import com.fagougou.xiaoben.model.Auth
import com.fagougou.xiaoben.model.AuthRequest
import com.fagougou.xiaoben.model.BotList
import com.fagougou.xiaoben.repo.Client.retrofitClient
import com.fagougou.xiaoben.utils.MMKV.kv
import com.iflytek.cloud.SpeechUtility
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class CommonApplication: Application(){
    companion object {
        var context: Context by Delegates.notNull()
        const val TAG = "FaGouGou@XiaoBen"
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
        context = this
        SpeechUtility.createUtility(this, "appid=33b963d0")
        MMKV.initialize(this)
        CoroutineScope(Dispatchers.IO).launch {
            val tokenResponse = retrofitClient.auth(AuthRequest()).execute()
            val tokenBody = tokenResponse.body() ?: Auth()
            kv.encode("token",tokenBody.data.token)
            val botListResponse = retrofitClient.botList().execute()
            val botListBody = botListResponse.body() ?: BotList()
            for(bot in botListBody.data)chatBotMap[bot.name]=bot.id
        }
    }
}
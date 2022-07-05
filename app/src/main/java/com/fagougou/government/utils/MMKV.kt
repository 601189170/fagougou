package com.fagougou.government.utils

import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.model.Auth
import com.fagougou.government.model.AuthRequest
import com.fagougou.government.model.BotList
import com.fagougou.government.model.SerialLoginResponse
import com.fagougou.government.repo.Client
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object MMKV {
    val appId = "appId"
    val appSec = "appSec"
    val mkt = "mkt"
    val robootSpeed = "robootSpeed"
    val token = "token"

    val kv = MMKV.defaultMMKV()
    val pdfKv = MMKV.mmkvWithID("pdfUpdate")

    fun setAuthData(it:SerialLoginResponse){
        kv.encode(appId, it.appId)
        kv.encode(appSec, it.appSec)
        kv.encode(mkt, it.mkt)
        CoroutineScope(Dispatchers.IO).launch {
            val tokenResponse = Client.apiService.auth(AuthRequest()).execute()
            val tokenBody = tokenResponse.body() ?: Auth()
            kv.encode(token, tokenBody.data.token)
            val botListResponse = Client.apiService.botList().execute()
            val botListBody = botListResponse.body() ?: BotList()
            for (bot in botListBody.data) ChatViewModel.botQueryIdMap[bot.name] = bot.id
        }
    }
}
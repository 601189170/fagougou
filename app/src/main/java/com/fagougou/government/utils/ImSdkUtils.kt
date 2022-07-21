package com.fagougou.government.utils

import android.content.Context
import cn.udesk.UdeskSDKManager
import cn.udesk.config.UdeskConfig
import java.util.*


object ImSdkUtils {

    var userName = "用户名"
    var userId = "用户id"

    private val UDESK_DOMAIN = "fagoug.s4.udesk.cn"

    //替换成你们生成应用产生的appid
    private val AppId = "3dca23c4bd227639"

    // 替换成你们在后台生成的密钥
    private val UDESK_SECRETKEY = "11a29962c6e648e35e4c650b0ae8dd95"

    fun startChart(context: Context){
        //咨询会话
        UdeskSDKManager.getInstance().entryChat(context, UdeskConfig.createDefualt(), userId)
    }

    fun initUdeskSDK(context: Context){
        //初始化
        UdeskSDKManager.getInstance().initApiKey(context,UDESK_DOMAIN,UDESK_SECRETKEY,AppId)
    }


}
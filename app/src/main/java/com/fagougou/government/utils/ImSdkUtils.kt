package com.fagougou.government.utils

import android.content.Context
import android.content.Intent
import com.fagougou.government.consult.TouristsLoginActivity
import com.m7.imkfsdk.KfStartHelper
import com.moor.imkf.IMChatManager
import com.moor.imkf.requesturl.RequestUrl
import org.json.JSONException
import org.json.JSONObject

object ImSdkUtils {
    var helper: KfStartHelper? = null
    val accessId = "90d8c6e0-d0d5-11ec-94aa-a36b72b57644"
    var userName = "用户名"
    var userId = "用户id"

     fun initKfHelper() {
        //设置sdk 显示语言版本
//        initLanguage();
        /*
          第一步：在APPlication中初始化
          第二步:初始化help
         */
        helper = KfStartHelper.getInstance()
        /**
         * 第三步：设置服务环境
         * setRequestUrl() 与 setRequestBasic()，为二选一调用
         * 腾讯云，阿里云用户只使用setRequestBasic即可。
         * 私有云用户只使用setRequestUrl即可。
         */

//        /**1:
//         * 设置服务环境 阿里 或 腾讯,可以联系技术支持确认您的服务环境
//         * 要在helper.initSdkChat()之前设置
//         * RequestUrl.ALIYUN_REQUEST;//阿里云环境
//         * RequestUrl.TENCENT_REQUEST;//腾讯云环境
//         */
        RequestUrl.setRequestBasic(RequestUrl.TENCENT_REQUEST)
        /**2:
         * 开放给私有云客户 设置地址的接口
         * 要在helper.initSdkChat()之前设置
         */
//        RequestUrl.setRequestUrl();
        /**2.1
         * 开放给私有云客户 设置文件服务地址的接口，如果私有云后端配置了文件服务器则需要调用。
         * 要在helper.initSdkChat()之前设置
         * 示例：RequestUrl.setFileUrl( "https://im-fileserver:8000/",new String[]{"im-fileserver:8000"},true)
         */
//        RequestUrl.setFileUrl( "完整的服务地址",new String[]{"只需填写中间域名以及端口部分"},是否为https);
    }

    /**
     * 初始化SDK
     */
     fun initSdk(helper: KfStartHelper) {
        /*
          商品信息实例，若有需要，请参照此方法；
        */
//        handleCardInfo();

        /*
          新卡片类型示例，若有需要，请参照此方法；
         */
//        handleNewCardInfo();
        setOtherParams() //添加配置拓展参数，如需使用注意要在initSdkChat之前之前用

        /*
          第三步:设置参数
          初始化sdk方法，必须先调用该方法进行初始化后才能使用IM相关功能
          @param accessId       接入id（需后台配置获取）
          @param userName       用户名
          @param userId         用户id
         */helper.initSdkChat(accessId, userName, userId)
    }

    //添加拓展参数，注意要在 initSdkChat之前之前用
     fun setOtherParams() {
        try {
            val `object` = JSONObject()
            `object`.put("test05", "test05")
            //            object.put("test02", "test02");
            val userlabel = JSONObject()
            //            userlabel.put("userlabel1", "userlabel1");
            IMChatManager.getInstance().setUserOtherParams("", `object`, true, userlabel)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
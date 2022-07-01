package com.fagougou.government.utils

import com.fagougou.government.CommonApplication
import com.fagougou.government.chatPage.ChatViewModel
import com.umeng.analytics.MobclickAgent

object UMConstans {
    //空闲返回首页
    var home_page="overtime_homepage"

    //进入法律咨询
    var home_ask="into_qa"

    //进入智能文书
    var home_generate_contract="into_smartdoc"

    //进入合同文库
    var home_document="into_docdb"

    //进入法律计算器
    var home_calculator="into_calc"

    //选择法律咨询领域
    var home_ask_area="into_qa_area"

    //进入人工咨询
    var home_dd="into_dd"

    //普通埋点
    fun setIntoClick(eventId:String){
        MobclickAgent.onEvent(CommonApplication.activity, eventId)
    }
    //带参埋点
    fun setIntoClickByArea(eventId:String,value:String){
        val music: MutableMap<String, Any> = HashMap()
        music["area"] = value
        MobclickAgent.onEventObject(CommonApplication.activity, eventId, music)
    }
}
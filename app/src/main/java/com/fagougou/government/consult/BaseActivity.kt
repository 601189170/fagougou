package com.fagougou.xiaoben.consult

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.databinding.ActivityChooseDomainBinding
import com.fagougou.government.databinding.ActivityWaitBinding
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.Time
import com.m7.imkfsdk.chat.MessageEvent
import com.m7.imkfsdk.utils.SpacesItemDecoration
import com.m7.imkfsdk.utils.statusbar.StatusBarUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Time.hideSystemUI()

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Router.lastTouchTime = Time.stampL
        return super.dispatchTouchEvent(ev)
    }



}
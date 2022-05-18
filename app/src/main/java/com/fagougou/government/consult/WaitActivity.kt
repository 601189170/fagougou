package com.fagougou.xiaoben.consult

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.fagougou.government.R
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

class WaitActivity : AppCompatActivity() {
    private var binding: ActivityWaitBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWaitBinding.inflate(getLayoutInflater());
        if (binding!=null){
            val rootView: View = binding!!.root
            setContentView(rootView)
        }
        StatusBarUtils.setColor(this, resources.getColor(R.color.white))
        ImSdkUtils.initKfHelper()
        ImSdkUtils.helper?.let {
            ImSdkUtils.initSdk(it)
        }
        Time.hideSystemUI()
        EventBus.getDefault().register(this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(messageEvent: MessageEvent){
        if (messageEvent.message.equals("1")){
            finish()
            ActivityUtils.finishActivity(this)
        }else if(messageEvent.message.equals("2")){
            finish()
            ActivityUtils.finishActivity(this)
            val intent = Intent(this, WaitActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this);//反注册EventBus
    }




}
package com.fagougou.government.consult

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.udesk.eventBus.MessageConstans.*
import cn.udesk.eventBus.MessageEvent
import com.blankj.utilcode.util.ActivityUtils
import com.fagougou.government.CommonApplication
import com.fagougou.government.MainActivity

import com.fagougou.government.Router
import com.fagougou.government.databinding.ActivityWaitBinding

import com.fagougou.government.utils.ImSdkUtils.startChart
import com.fagougou.government.utils.Time

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WaitActivity : AppCompatActivity() {
    lateinit var binding: ActivityWaitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonApplication.activity = this
        binding = ActivityWaitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startChart(this)
        binding.leftBtn.setOnClickListener {}
        binding.rightBtn.setOnClickListener { finish() }
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(messageEvent: MessageEvent){
        when(messageEvent.message){
            CloseWait->{ finish() }

            RefreshTime->{ Router.lastTouchTime = Time.stamp }

            CloseToMain->{
                ActivityUtils.finishToActivity(MainActivity::class.java, false)
                Router.lastTouchTime = 0
            }
            CloseToWait->{
                finish()
                val intent = Intent(this, WaitActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)//反注册EventBus
    }

}
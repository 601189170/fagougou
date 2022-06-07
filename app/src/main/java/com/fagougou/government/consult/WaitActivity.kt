package com.fagougou.government.consult

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.fagougou.government.MainActivity

import com.fagougou.government.Router
import com.fagougou.government.databinding.ActivityWaitBinding
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.Time
import com.m7.imkfsdk.MessageConstans
import com.m7.imkfsdk.chat.MessageEvent
import com.m7.imkfsdk.video.YKFCallManager
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
        ImSdkUtils.initKfHelper()
        ImSdkUtils.helper?.let {
            ImSdkUtils.initSdk(it)
        }
        YKFCallManager.cameraRotation=0
        binding!!.leftBtn.setOnClickListener {

        }
        binding!!.rightBtn.setOnClickListener {
            finish()
        }
        EventBus.getDefault().register(this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(messageEvent: MessageEvent){
        if (messageEvent.message .equals(MessageConstans.CloseToMain) ) {
            ActivityUtils.finishToActivity(MainActivity::class.java, false)
            Router.lastTouchTime = 0
        }else if (messageEvent.message.equals(MessageConstans.CloseWait)){
            finish()
        }else if(messageEvent.message.equals(MessageConstans.CloseToWait)){
            finish()
            val intent = Intent(this, WaitActivity::class.java)
            startActivity(intent)
        }else if(messageEvent.message.equals(MessageConstans.RefreshTime)){
            Router.lastTouchTime = Time.stampL
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

}
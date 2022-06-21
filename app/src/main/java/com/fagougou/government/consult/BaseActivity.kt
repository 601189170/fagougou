package com.fagougou.government.consult

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.fagougou.government.CommonApplication
import com.fagougou.government.MainActivity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.CommonApplication.Companion.presentation
import com.fagougou.government.utils.Time
import com.m7.imkfsdk.MessageConstans.*
import com.m7.imkfsdk.chat.MessageEvent
import com.m7.imkfsdk.chat.dialog.TaskTimeBaseDialog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseActivity : AppCompatActivity() {

    var diallog: TaskTimeBaseDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonApplication.activity = this
        EventBus.getDefault().register(this)
         diallog=TaskTimeBaseDialog(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Router.lastTouchTime = Time.stampL
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        presentation?.mediaPlayer?.pause()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEventMainThread(messageEvent: MessageEvent) {
        when(messageEvent.message){
            CloseToMain ->{
                ActivityUtils.finishToActivity(MainActivity::class.java, false)
                Router.lastTouchTime = 0
            }
            CloseToWait ->{
                finish()
                val intent = Intent(this, WaitActivity::class.java)
                startActivity(intent)
            }
            CloseWait ->{ finish() }
            RefreshTime ->{ Router.lastTouchTime = Time.stampL }
            PalyVideoHumanAre ->{ presentation?.playVideo(R.raw.vh_human_area) }
            PalyVideoHuman -> { presentation?.playVideo(R.raw.vh_human) }
            CloseAction -> { if (diallog?.isShowing==false &&!isFinishing){ diallog?.RefreshShow() } }
        }
    }
}
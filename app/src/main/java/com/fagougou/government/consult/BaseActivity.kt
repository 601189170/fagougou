package com.fagougou.government.consult

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ActivityUtils
import com.fagougou.government.CommonApplication
import com.fagougou.government.MainActivity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.presentation.BannerPresentation
import com.fagougou.government.presentation.BannerPresentation.Companion.restartVideo
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.ZYSJ
import com.m7.imkfsdk.MessageConstans
import com.m7.imkfsdk.MessageConstans.*
import com.m7.imkfsdk.chat.MessageEvent
import com.m7.imkfsdk.chat.dialog.TaskTimeBaseDialog
import com.m7.imkfsdk.chat.dialog.TimeoDialogListener

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseActivity : AppCompatActivity() {

    var diallog: TaskTimeBaseDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Time.hideSystemUI()
        EventBus.getDefault().register(this)
         diallog=TaskTimeBaseDialog(this)

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Router.lastTouchTime = Time.stampL
        return super.dispatchTouchEvent(ev)
    }


    protected open fun setStatusBar(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.statusBarColor = color
            window.navigationBarColor = color
            var vis = window.decorView.systemUiVisibility
            vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            vis = vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.decorView.systemUiVisibility = vis
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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

            PalyVideoHumanAre ->{ restartVideo(R.raw.vh_human_area) }

            PalyVideoHuman -> { restartVideo(R.raw.vh_human) }

            CloseAction -> { if (diallog?.isShowing==false &&!isFinishing){ diallog?.RefreshShow() } }
        }


    }




}
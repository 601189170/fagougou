package com.fagougou.xiaoben.consult

import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.utils.Time

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Time.hideSystemUI()
//        StatusBarUtils.setColor(this, resources.getColor(R.color.ykfsdk_all_white))
        setStatusBar(resources.getColor(R.color.white))

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
}
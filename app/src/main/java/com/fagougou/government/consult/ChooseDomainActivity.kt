package com.fagougou.xiaoben.consult

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fagougou.government.R
import com.fagougou.government.utils.ImSdkUtils

import com.m7.imkfsdk.chat.KFBaseActivity
import com.m7.imkfsdk.utils.statusbar.StatusBarUtils

class ChooseDomainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_domain)
        StatusBarUtils.setColor(this, resources.getColor(R.color.white))
        ImSdkUtils.initKfHelper()

    }

    @Override
    fun onClick(v: View) {
        if (v.id == R.id.fillStart) {
            //初始化SDK
            ImSdkUtils.helper?.let {
                ImSdkUtils.initSdk(it)
            }
        }
    }

}
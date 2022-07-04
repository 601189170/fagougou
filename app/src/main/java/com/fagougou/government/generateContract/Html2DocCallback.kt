package com.fagougou.government.generateContract

import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.utils.Tips
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class Html2DocCallback: Callback {
    override fun onResponse(call: Call, response: Response) {
        if (response.code==200) {
            val url = response.body?.string() ?: ""
            QrCodeViewModel.set(url,"微信查看")
        }else Tips.toast("转码失败，请重试")
    }

    override fun onFailure(call: Call, e: IOException) {
        Timber.e(e)
    }
}
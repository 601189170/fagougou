package com.fagougou.government.utils

import android.graphics.Color
import androidx.compose.runtime.mutableStateOf
import com.fagougou.government.utils.MMKV.kv
import com.king.zxing.util.CodeUtils

object Wechat {
    val showQrCode = mutableStateOf(false)
    fun wechatBitmap() = CodeUtils.createQRCode(kv.decodeString("wechatUrl"), 256, null, Color.BLACK)
}
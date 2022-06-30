package com.fagougou.government.component

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color as ColorCompose
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.utils.MMKV
import com.king.zxing.util.CodeUtils

object QrCodeViewModel {
    private val _content = mutableStateOf("")
    private val _hint = mutableStateOf("")
    val content by _content
    val hint by _hint

    fun constWechatUrl():String{
        val mkt = MMKV.kv.decodeString(MMKV.mkt,"")?:""
        return "https://m.fagougou.com/wx/custom?mkt=$mkt"
    }

    fun set(url:String,hint:String){
        _content.value = url
        _hint.value = hint
    }

    fun bitmap(url: String = content): Bitmap = CodeUtils.createQRCode(url, 200, null, Color.BLACK)

    fun clear(){
        _content.value = ""
        _hint.value = ""
    }
}

@Composable
fun GlobalQrCode(){
    if(QrCodeViewModel.content.isNotBlank()) Surface( color = ColorCompose(0x33000000)) {
        Column(
            Modifier.fillMaxSize().clickable { QrCodeViewModel.clear() },
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Surface(
                Modifier.width(272.dp),
                shape = RoundedCornerShape(CORNER_FLOAT),
                color = ColorCompose(0xFFFFFFFF)
            ) {
                Column(
                     horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(Modifier.width(200.dp).padding(top = 24.dp),color = ColorCompose.Transparent) {
                        Image(QrCodeViewModel.bitmap().asImageBitmap(),null)
                    }
                    Text(QrCodeViewModel.hint, Modifier.padding(vertical = 16.dp),fontSize = 28.sp)
                }
            }
            Image(painterResource(R.drawable.ic_close),null,Modifier.padding(32.dp))
        }
    }
}



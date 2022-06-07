package com.fagougou.government.component

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.king.zxing.util.CodeUtils

object QrCodeViewModel {
    const val constWechatUrl="https://m.fagougou.com/wx/custom?mkt=ideil0957f3ae"
    val content = mutableStateOf("")
    val hint = mutableStateOf("")

    fun bitmap() = CodeUtils.createQRCode(content.value, 256, null, Color.BLACK)

    fun clear(){
        content.value = ""
        hint.value = ""
    }
}

@Composable
fun QrCode(){
    if(QrCodeViewModel.content.value.isNotBlank()) Surface( color = ColorCompose(0x33000000)) {
        Column(
            modifier = Modifier.fillMaxSize().clickable { QrCodeViewModel.clear() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Surface(
                modifier = Modifier

                    .width(272.dp)
                    .height(320.dp),
                shape = RoundedCornerShape(CORNER_FLOAT),
                color = ColorCompose(0xFFFFFFFF)
            ) {
                Column(
                     horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(Modifier.width(210.dp).padding(top = 15.dp),color = androidx.compose.ui.graphics.Color.Transparent) {
                        Image(QrCodeViewModel.bitmap().asImageBitmap(),null)
                    }
                    Text(QrCodeViewModel.hint.value, fontSize = 28.sp)
                }
            }
            Image(modifier = Modifier.padding(32.dp),painter = painterResource(R.drawable.ic_close), contentDescription = null)
        }
    }
}
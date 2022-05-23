package com.fagougou.government.qrCode

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
    val show = mutableStateOf(false)
    val constWechatUrl="https://m.fagougou.com/wx/custom?mkt=ideil0957f3ae"
    var currentUrl = constWechatUrl
    fun bitmap() = CodeUtils.createQRCode(currentUrl, 256, null, Color.BLACK)
}

@Composable
fun QrCode(){
    if(QrCodeViewModel.show.value) Surface( color = ColorCompose(0x33000000)) {
        Button(
            onClick = { QrCodeViewModel.show.value = false },
            content = {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Surface(
                        modifier = Modifier
                            .width(272.dp)
                            .height(320.dp),
                        shape = RoundedCornerShape(CORNER_FLOAT),
                        color = ColorCompose(0xFFFFFFFF)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(QrCodeViewModel.bitmap().asImageBitmap(),null)
                            Text("扫一扫查看", fontSize = 28.sp, modifier = Modifier.padding(16.dp))
                        }
                    }
                    Image(modifier = Modifier.padding(32.dp),painter = painterResource(R.drawable.ic_close), contentDescription = null)
                }
            },
            colors = ButtonDefaults.buttonColors(ColorCompose.Transparent),
            elevation = ButtonDefaults.elevation(0.dp)
        )
    }
}
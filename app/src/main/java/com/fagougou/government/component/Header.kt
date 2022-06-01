package com.fagougou.government.component

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router.routeMirror
import com.fagougou.government.consult.TouristsLoginActivity
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.SafeBack.safeBack

@Composable
fun Header(
    title:String,
    navController: NavController,
    onBack:() -> Unit = {},
    canClose:Boolean = true,
    qrCode:String = "",
    qrCodeHint:String = "微信扫码咨询"
){
    Surface(color = Color(0xFF17192C)) {
        Row(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .width(200.dp)
                    .clickable {
                        onBack.invoke()
                        if (canClose) navController.safeBack()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp),
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back"
                )
                BasicText("返回")
            }
            BasicText(title)
            Row(
                Modifier.width(200.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if(qrCode.isNotBlank()) {
                    Image(painterResource(id = R.drawable.ic_wechat), null)
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                QrCodeViewModel.content.value = qrCode
                                QrCodeViewModel.hint.value = qrCodeHint
                            },
                        text = "微信",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
                if (routeMirror.contains("chat")) {
                    Image(painterResource(id = R.drawable.ic_human), null)
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                IFly.stopAll()
                                val intent = Intent(activity, TouristsLoginActivity::class.java)
                                activity.startActivity(intent)
                            },
                        text = "人工",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))
            }
        }
    }
}
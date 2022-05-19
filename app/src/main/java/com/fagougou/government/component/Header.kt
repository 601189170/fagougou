package com.fagougou.government.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import com.fagougou.government.R
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.SafeBack.safeBack
import com.fagougou.government.utils.Tips
import com.fagougou.government.wechat.Wechat

@Composable
fun Header(title:String, navController: NavController, onBack:() -> Unit = {}, canClose:Boolean = true){
    Surface(color = Color(0xFF17192C)) {
        Row(
            modifier = Modifier
                .height(72.dp)
                .padding(top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .width(230.dp)
                    .clickable {
                        onBack.invoke()
                        if(canClose)navController.safeBack()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp),
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back"
                )
                Text("返回", fontSize = 24.sp, color = Color.White)
            }
            Text( title, color = Color.White, fontSize = 24.sp )
            Surface( Modifier.width(230.dp), color = Color.Transparent ) {
                if (navController.currentDestination?.route?.contains("chat") == true) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(id = R.drawable.ic_wechat), null)
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            shape = RoundedCornerShape(
                                topEnd = CORNER_FLOAT,
                                bottomEnd = CORNER_FLOAT
                            ),
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                            content = {
                                Text(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    text = "微信",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            },
                            onClick = { Wechat.showQrCode.value = true }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Image(painterResource(id = R.drawable.ic_human), null)
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            shape = RoundedCornerShape(
                                topEnd = CORNER_FLOAT,
                                bottomEnd = CORNER_FLOAT
                            ),
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                            content = {
                                Text(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    text = "人工",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            },
                            onClick = {
                                ImSdkUtils.startAc(Tips.context)
                            }
                        )
                    }
                }
            }
        }
    }
}
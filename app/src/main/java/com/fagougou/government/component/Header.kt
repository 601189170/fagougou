package com.fagougou.government.component

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.Router.routeMirror
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.consult.TouristsLoginActivity
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.SafeBack.safeBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Header(
    title:String,
    navController: NavController,
    onBack:() -> Unit = {},
    canClose:Boolean = true,
    qrCode:String = "",
    qrCodeHint:String = "微信扫码咨询",
    isCases:Boolean = false
){
    val scope = rememberCoroutineScope()
    Surface(
        Modifier.height(64.dp).fillMaxWidth(),
        color = Color(0xFF17192C)
    ) {
        Row(
            Modifier.height(64.dp).fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically,
        ) {
            Row(
                Modifier.width(240.dp).clickable {
                    onBack.invoke()
                    if (canClose) navController.safeBack()
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ic_back),
                    "Back",
                    Modifier.padding(start = 24.dp, end = 12.dp)
                )
                BasicText("返回")
            }
            BasicText(title)
            Row(
                Modifier.width(240.dp).padding(end = 12.dp),
                Arrangement.End,
                Alignment.CenterVertically
            ) {
                if(qrCode.isNotBlank()) Row(
                    Modifier
                        .fillMaxHeight()
                        .clickable { QrCodeViewModel.set(qrCode,qrCodeHint) },
                    Arrangement.Start,
                    Alignment.CenterVertically
                ){
                    Image(painterResource(R.drawable.ic_wechat), null)
                    Text("微信", Modifier.padding(start = 8.dp), Color.White, 24.sp,)
                }
                Spacer(Modifier.width(24.dp))
                if (routeMirror.contains("chat")) Row(
                    Modifier.fillMaxHeight().clickable {
                        IFly.stopAll()
                        if (routeMirror == Router.chat)  scope.launch(Dispatchers.Default) {
                            delay(200)
                            withContext(Dispatchers.Main){ navController.safeBack() }
                        }
                        val intent = Intent(activity, TouristsLoginActivity::class.java)
                        activity.startActivity(intent)
                    },
                    Arrangement.Start,
                    Alignment.CenterVertically
                ){
                    Image(painterResource(id = R.drawable.ic_human), null)
                    Text("人工", Modifier.padding(start = 8.dp), Color.White, 24.sp)
                }
                if (isCases)
                Row(Modifier.fillMaxHeight().clickable {
                    with(DialogViewModel) {
                        clear()
                        DialogViewModel.title = "审查报告示例"
                        type = "cases"
                        content.add( ContentStyle( "审查报告示例" ) )
                    }
                },
                    Arrangement.Start,
                    Alignment.CenterVertically
                ){
                    Image(painterResource(id = R.drawable.ic_icon_cases), null)
                    Text("审查示例", Modifier.padding(start = 8.dp), Color.White, 24.sp)
                }
                Spacer(Modifier.width(32.dp))
            }
        }
    }
}
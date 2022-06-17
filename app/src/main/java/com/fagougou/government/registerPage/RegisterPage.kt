package com.fagougou.government.registerPage

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.component.BasicText
import com.fagougou.government.model.*
import com.fagougou.government.registerPage.RegisterViewModel.login
import com.fagougou.government.registerPage.RegisterViewModel.registerCode
import com.fagougou.government.registerPage.RegisterViewModel.registerAction
import com.fagougou.government.registerPage.RegisterViewModel.registerBalance
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.callBack
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.mainRegister
import com.fagougou.government.ui.theme.Alpha33WhiteTextFieldColor
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.MMKV
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.ZYSJ
import kotlinx.coroutines.*

@Composable
fun RegisterPage(navController: NavController){
    LaunchedEffect(null){
        Time.hook["AutoCheckRegister"]={
            mainRegister.login(SerialLoginRequest(CommonApplication.serial)).enqueue(
                callBack {
                    if(it.canLogin){
                        Time.hook.remove("AutoCheckRegister")
                        kv.encode(MMKV.appId,it.appId)
                        kv.encode(MMKV.appSec,it.appSec)
                        kv.encode(MMKV.mkt,it.mkt)
                        CoroutineScope(Dispatchers.IO).launch {
                            val tokenResponse = Client.apiService.auth(AuthRequest()).execute()
                            val tokenBody = tokenResponse.body() ?: Auth()
                            kv.encode("token", tokenBody.data.token)
                            val botListResponse = Client.apiService.botList().execute()
                            val botListBody = botListResponse.body() ?: BotList()
                            for (bot in botListBody.data) if (bot.tyId == "") ChatViewModel.botQueryIdMap[bot.name] = bot.id
                        }
                        navController.navigate(Router.home)
                    }
                }
            )
        }
    }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Top,
        Alignment.CenterHorizontally,
        ) {
        Row(
            Modifier.fillMaxWidth().height(48.dp).padding(top = 16.dp,start = 40.dp,end = 40.dp),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.home_logo),
                "Company Logo",
                Modifier.height(32.dp)
            )
            BasicText(Time.timeText.value,0.dp,24.sp)
        }
        BasicText("注册码绑定",192.dp,32.sp)
        TextField(
            modifier = Modifier
                .padding(top = 40.dp)
                .width(480.dp),
            value = registerCode.value,
            onValueChange = {registerCode.value = it},
            textStyle = TextStyle(fontSize = 28.sp),
            placeholder = {Text("请输入注册码",color = Color.Gray, fontSize = 28.sp)},
            shape = RoundedCornerShape(CORNER_FLOAT),
            colors = Alpha33WhiteTextFieldColor(),
            leadingIcon = { Image(painterResource(R.drawable.ic_key), null, modifier = Modifier.padding(horizontal = 24.dp))}
        )
        Button(
            { if(registerAction.value=="立即绑定") login(navController) },
            Modifier
                .padding(top = 24.dp)
                .width(480.dp)
                .height(60.dp),
            content = {
                Text(
                    text = registerAction.value,
                    color = Color.White,
                    fontSize = 28.sp
                )
            },
            colors = ButtonDefaults.buttonColors(Dodgerblue)
        )
        Text(
            "技术支持：法狗狗(深圳)科技有限公司 ${CommonApplication.serial}",
            Modifier
                .padding(top = 460.dp)
                .clickable {
                    Time.exitStack--
                    if (Time.exitStack <=0){
                        Time.exitStack =8
                        ZYSJ.showBar()
                        activity.finish()
                    }
                },
            Color.White,
            24.sp,
        )
    }
    BackHandler {}
}
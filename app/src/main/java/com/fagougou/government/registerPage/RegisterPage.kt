package com.fagougou.government.registerPage

import android.content.Context
import android.os.Build
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
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.registerPage.RegisterViewModel.login
import com.fagougou.government.registerPage.RegisterViewModel.registerCode
import com.fagougou.government.registerPage.RegisterViewModel.registerAction
import com.fagougou.government.registerPage.RegisterViewModel.registerBalance
import com.fagougou.government.model.SerialLoginRequest
import com.fagougou.government.model.SerialLoginResponse
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.mainRegister
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.MMKV
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*

@Composable
fun RegisterPage(context: Context, navController: NavController){
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        scope.launch(Dispatchers.IO){
            while(isActive){
                delay(3000)
                var body = SerialLoginResponse()
                try {
                    val response = mainRegister.login(SerialLoginRequest(Build.SERIAL)).execute()
                    body = response.body() ?: SerialLoginResponse()
                }catch (e:Exception){
                    handleException(e)
                }
                if(body.canLogin){
                    if(registerBalance.value < 0) withContext(Dispatchers.Main){
                        navController.navigate(Router.home)
                    }
                    break
                }
            }
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
        val textFieldColors = TextFieldDefaults.textFieldColors(
            Color.White,
            backgroundColor = Color(0x44FFFFFF),
            cursorColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
        TextField(
            modifier = Modifier
                .padding(top = 40.dp)
                .width(480.dp),
            value = registerCode.value,
            onValueChange = {registerCode.value = it},
            textStyle = TextStyle(fontSize = 28.sp),
            placeholder = {Text("请输入注册码",color = Color.Gray, fontSize = 28.sp)},
            shape = RoundedCornerShape(CORNER_FLOAT),
            colors = textFieldColors,
            leadingIcon = { Image(painterResource(R.drawable.ic_key), null, modifier = Modifier.padding(horizontal = 24.dp))}
        )
        Button(
            { if(registerAction.value=="立即绑定") login(context, navController) },
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
            "技术支持：法狗狗(深圳)科技有限公司 ${Build.SERIAL}",
            Modifier
                .padding(top = 460.dp)
                .clickable {
                    Time.exitStack--
                    if (Time.exitStack <=0){
                        Time.exitStack =8
                        activity.finish()
                    }
                },
            Color.White,
            24.sp,
        )
    }
    BackHandler {}
}
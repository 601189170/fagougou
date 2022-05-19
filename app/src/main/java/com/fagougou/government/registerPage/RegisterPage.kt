package com.fagougou.government.registerPage

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import com.fagougou.government.R
import com.fagougou.government.Router
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
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*

@Composable
fun RegisterPage(navController: NavController){
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.home_logo),
                contentDescription = "Home Logo",
                modifier = Modifier.height(36.dp)
            )
            Text(
                Time.timeText.value,
                color = Color.White,
                fontSize = 24.sp
            )
        }
        Text(
            modifier = Modifier.padding(top = 192.dp),
            text = "智能法律服务系统",
            color = Color.White,
            fontSize = 32.sp
        )
        val textFieldColors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0x44FFFFFF),
            cursorColor = Color.White,
            focusedIndicatorColor = Color.Transparent,

        )
        TextField(
            modifier = Modifier
                .padding(top = 40.dp)
                .width(480.dp),
            value = registerCode.value,
            onValueChange = {registerCode.value = it},
            textStyle = TextStyle(color = Color.White, fontSize = 28.sp),
            placeholder = {Text("请输入注册码",color = Color.Gray, fontSize = 28.sp)},
            shape = RoundedCornerShape(CORNER_FLOAT),
            colors = textFieldColors,
            leadingIcon = { Image(painterResource(R.drawable.ic_login_user), null, modifier = Modifier.padding(horizontal = 24.dp))}
        )
        Button(
            modifier = Modifier
                .padding(top = 24.dp)
                .width(480.dp)
                .height(60.dp),
            onClick = { if(registerAction.value=="立即绑定") login(navController) },
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
            modifier = Modifier.padding(top = 400.dp),
            text = "技术支持：法狗狗人工智能 v2.0 ${Build.SERIAL}",
            fontSize = 20.sp,
            color = Color.White
        )
    }
    BackHandler {

    }
}
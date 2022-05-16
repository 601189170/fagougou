package com.fagougou.government.loginPage

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.MainActivity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.loginPage.LoginPage.login
import com.fagougou.government.loginPage.LoginPage.register
import com.fagougou.government.loginPage.LoginPage.state
import com.fagougou.government.model.SerialRegisterResponse
import com.fagougou.government.model.SerialRegisterRequest
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.mainLogin
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

object LoginPage {
    val register = mutableStateOf("")
    val state = mutableStateOf("立即绑定")
    fun login(navController: NavController){
        state.value = "绑定中..."
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mainLogin.register(SerialRegisterRequest(Build.SERIAL,register.value)).execute()
                val body = response.body() ?: SerialRegisterResponse()
                if (body.balance<0){
                    toast(body.errorMessage)
                    return@launch
                }
                register.value = ""
                kv.encode("canLogin",true)
                kv.encode("wechatUrl","null")
                withContext(Dispatchers.Main) {
                    navController.navigate(Router.home)
                }
            } catch (e:Exception){
                handleException(e)
            }finally {
                state.value = "立即绑定"
            }
        }
    }
}

@Composable
fun LoginPage(navController: NavController){
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
            value = register.value,
            onValueChange = {register.value = it},
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
            onClick = { if(state.value=="立即绑定")login(navController) },
            content = {
                Text(
                    text = state.value,
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
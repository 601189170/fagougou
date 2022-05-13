package com.fagougou.government.loginPage

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.MainActivity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.loginPage.LoginPage.login
import com.fagougou.government.loginPage.LoginPage.password
import com.fagougou.government.loginPage.LoginPage.state
import com.fagougou.government.loginPage.LoginPage.userName
import com.fagougou.government.model.SerialLoginRequest
import com.fagougou.government.model.SerialLoginRespon
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.mainLogin
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips.toast
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

object LoginPage {
    val userName = mutableStateOf("")
    val password = mutableStateOf("")
    val state = mutableStateOf("立即登录")
    fun login(navController: NavController){
        state.value = "登录中..."
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mainLogin.login(SerialLoginRequest(Build.SERIAL)).execute()
                val body = response.body() ?: SerialLoginRespon()
                if (!body.canlogin){
                    toast("用户名或密码错误")
                    return@launch
                }
                userName.value = ""
                password.value = ""
                kv.encode("canLogin",true)
                kv.encode("wechatUrl","null")
                withContext(Dispatchers.Main) {
                    (activity as MainActivity).hideSystemUI()
                    navController.navigate(Router.home)
                }
            }catch (e:MalformedJsonException){
                toast("用户名或密码错误")
            } catch (e:Exception){
                handleException(e)
            }finally {
                state.value = "立即登录"
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
            value = userName.value,
            onValueChange = {userName.value = it},
            textStyle = TextStyle(color = Color.White, fontSize = 28.sp),
            placeholder = {Text("请输入账号",color = Color.Gray, fontSize = 28.sp)},
            shape = RoundedCornerShape(CORNER_FLOAT),
            colors = textFieldColors,
            leadingIcon = { Image(painterResource(R.drawable.ic_login_user), null, modifier = Modifier.padding(horizontal = 24.dp))}
        )
        TextField(
            modifier = Modifier
                .padding(top = 24.dp)
                .width(480.dp),
            value = password.value,
            onValueChange = { password.value = it},
            textStyle = TextStyle(color = Color.White, fontSize = 28.sp),
            placeholder = {Text("请输入密码",color = Color.Gray, fontSize = 28.sp)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(CORNER_FLOAT),
            colors = textFieldColors,
            leadingIcon = { Image(painterResource(R.drawable.ic_login_password), null, modifier = Modifier.padding(horizontal = 24.dp))}
        )
        Button(
            modifier = Modifier
                .padding(top = 24.dp)
                .width(480.dp)
                .height(80.dp),
            onClick = { if(state.value=="立即登录")login(navController) },
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
            modifier = Modifier.padding(top = 290.dp),
            text = "技术支持：法狗狗人工智能 v2.0",
            fontSize = 20.sp,
            color = Color.White
        )
    }
}
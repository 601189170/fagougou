package com.fagougou.xiaoben.loginPage

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
import com.fagougou.xiaoben.CommonApplication.Companion.activityContext
import com.fagougou.xiaoben.MainActivity
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.loginPage.LoginPage.login
import com.fagougou.xiaoben.loginPage.LoginPage.password
import com.fagougou.xiaoben.loginPage.LoginPage.state
import com.fagougou.xiaoben.loginPage.LoginPage.userName
import com.fagougou.xiaoben.model.User
import com.fagougou.xiaoben.repo.Client.handleException
import com.fagougou.xiaoben.repo.Client.mainLogin
import com.fagougou.xiaoben.ui.theme.CORNER_FLOAT
import com.fagougou.xiaoben.ui.theme.Dodgerblue
import com.fagougou.xiaoben.utils.MMKV.kv
import com.fagougou.xiaoben.utils.Time
import com.fagougou.xiaoben.utils.Tips.toast
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

object LoginPage {
    val userName = mutableStateOf("")
    val password = mutableStateOf("")
    val state = mutableStateOf("登录")
    fun login(navController: NavController){
        state.value = "登录中..."
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mainLogin.login(User(userName.value, password.value)).execute().body()
                kv.encode("canLogin",true)
                (activityContext as MainActivity).hideSystemUI()
                withContext(Dispatchers.Main) { navController.navigate("home") }
            }catch (e:MalformedJsonException){
                toast("用户名或密码错误")
            } catch (e:Exception){
                handleException(e)
            }finally {
                state.value = "登录"
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
            text = "登录账号",
            color = Color.White,
            fontSize = 56.sp
        )
        val textFieldColors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0x44FFFFFF),
            cursorColor = Color.White,
            focusedIndicatorColor = Color.Transparent,

        )
        TextField(
            modifier = Modifier
                .padding(top = 64.dp)
                .width(720.dp),
            value = userName.value,
            onValueChange = {userName.value = it},
            textStyle = TextStyle(color = Color.White, fontSize = 42.sp),
            placeholder = {Text("请输入账号",color = Color.Gray, fontSize = 42.sp)},
            shape = RoundedCornerShape(CORNER_FLOAT),
            colors = textFieldColors,
            leadingIcon = { Image(painterResource(R.drawable.ic_login_user), null, modifier = Modifier.padding(horizontal = 24.dp))}
        )
        TextField(
            modifier = Modifier
                .padding(top = 48.dp)
                .width(720.dp),
            value = password.value,
            onValueChange = { password.value = it},
            textStyle = TextStyle(color = Color.White, fontSize = 42.sp),
            placeholder = {Text("请输入密码",color = Color.Gray, fontSize = 42.sp)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(CORNER_FLOAT),
            colors = textFieldColors,
            leadingIcon = { Image(painterResource(R.drawable.ic_login_password), null, modifier = Modifier.padding(horizontal = 24.dp))}
        )
        Button(
            modifier = Modifier
                .padding(top = 48.dp)
                .width(720.dp)
                .height(100.dp),
            onClick = { if(state.value=="登录")login(navController) },
            content = {
                Text(
                    text = state.value,
                    color = Color.White,
                    fontSize = 42.sp
                )
            },
            colors = ButtonDefaults.buttonColors(Dodgerblue)
        )
    }
}
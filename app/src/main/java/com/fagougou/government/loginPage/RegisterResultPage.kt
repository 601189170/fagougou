package com.fagougou.government.loginPage

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.loginPage.LoginPageViewModel.registerBalance
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.Time

@Composable
fun RegisterResultPage(navController: NavController){
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
        Image(
            modifier = Modifier.padding(top = 192.dp),
            painter = painterResource(R.drawable.ic_success),
            contentDescription = "Home Logo",
        )
        Text(
            modifier = Modifier.padding(top = 48.dp),
            text = "绑定成功，该注册码还可绑定${registerBalance.value}台设备",
            color = Color.White,
            fontSize = 32.sp
        )
        Button(
            modifier = Modifier
                .padding(top = 48.dp)
                .width(480.dp)
                .height(60.dp),
            onClick = { navController.navigate(Router.home) },
            content = {
                Text(
                    text = "进入首页",
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
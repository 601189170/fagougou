package com.fagougou.government.homePage

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.utils.MMKV.clearStack
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.Time.timeText
import com.fagougou.government.utils.Tips.toast

@Composable
fun HomeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentId: Int
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(CORNER_FLOAT),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        content = {
            Image(
                painter = painterResource(contentId),
                contentDescription = "HomeButton"
            )
        }
    )
}

@Composable
fun HomePage(navController:NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,

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
                timeText.value,
                color = Color.White,
                fontSize = 24.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(horizontal = 36.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 48.dp),
                text = "欢迎使用",
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                modifier = Modifier.padding(bottom = 72.dp),
                text = "智能法律服务系统",
                fontSize = 27.sp,
                color = Color.White
            )
            Row(
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                HomeButton(
                    modifier = Modifier
                        .width(424.dp)
                        .height(264.dp),
                    onClick = { navController.navigate(Router.chatGuide) },
                    contentId = R.drawable.home_law_ask)
                HomeButton(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .width(200.dp)
                        .height(264.dp),
                    onClick = { navController.navigate(Router.contract) },
                    contentId = R.drawable.home_document)
                HomeButton(
                    modifier = Modifier
                        .width(200.dp)
                        .height(264.dp),
                    onClick = { navController.navigate(Router.generateContract) },
                    contentId = R.drawable.home_statistic)
            }
            Row(
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                HomeButton(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .width(424.dp)
                        .height(120.dp),
                    onClick = {  navController.navigate(Router.calculator)  },
                    contentId = R.drawable.home_law_calculator)
                HomeButton(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .width(424.dp)
                        .height(120.dp),
                    onClick = { navController.navigate(Router.about) },
                    contentId = R.drawable.home_about_us)
            }
        }
        Button(
            onClick = {
                clearStack--
                if (clearStack<=0){
                    kv.remove("canLogin")
                    toast("登出成功")
                    activity.finish()
                }
            },
            content = {
                Text(
                    modifier = Modifier.padding(vertical = 24.dp),
                    text = "技术支持：法狗狗人工智能 v2.0",
                    fontSize = 24.sp,
                    color = Color.White
                )
            },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            elevation = ButtonDefaults.elevation(0.dp)
        )
    }
}
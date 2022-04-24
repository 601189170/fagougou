package com.fagougou.xiaoben

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.xiaoben.chatPage.ChatPage
import com.fagougou.xiaoben.chatPage.GuidePage
import com.fagougou.xiaoben.homePage.HomePage
import com.fagougou.xiaoben.statisticPage.StatisticPage
import com.fagougou.xiaoben.ui.theme.XiaoBenTheme
import com.google.accompanist.pager.ExperimentalPagerApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
        setContent {
            XiaoBenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) { Main() }
            }
        }
    }

    fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.home_background),
        contentDescription = "Background"
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.fillMaxHeight()
        ) {
            composable("home") { HomePage(navController) }
            composable("guide") { GuidePage(navController) }
            composable("chat") { ChatPage(navController) }
            composable("statistic") { StatisticPage(navController) }
        }
    }
}

@Composable
fun Headder(title:String,navController: NavController,onBack:() -> Unit = {}){
    Surface(color = Color(0xFF17192C)) {
        Row(
            modifier = Modifier
                .height(96.dp).padding(top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                onClick = {
                    onBack.invoke()
                    navController.popBackStack()
                },
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            modifier = Modifier.padding(start = 24.dp, end = 12.dp),
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                        Text("返回", fontSize = 26.sp, color = Color.White)
                    }

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            )
        }
        Row(
            modifier = Modifier
                .height(96.dp).padding(top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                title,
                color = Color.White,
                fontSize = 30.sp
            )
        }
    }
}
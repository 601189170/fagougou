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
import com.fagougou.xiaoben.CommonApplication.Companion.activityContext
import com.fagougou.xiaoben.SafeBack.safeBack
import com.fagougou.xiaoben.aboutUsPage.AboutUs
import com.fagougou.xiaoben.calculatorPage.CalculatorGuidePage
import com.fagougou.xiaoben.chatPage.ChatPage
import com.fagougou.xiaoben.chatPage.ChatGuidePage
import com.fagougou.xiaoben.contractPage.ContractGuidePage
import com.fagougou.xiaoben.homePage.HomePage
import com.fagougou.xiaoben.loginPage.LoginPage
import com.fagougou.xiaoben.statisticPage.StatisticPage
import com.fagougou.xiaoben.ui.theme.XiaoBenTheme
import com.fagougou.xiaoben.utils.MMKV.kv
import com.fagougou.xiaoben.webViewPage.WebViewPage
import com.google.accompanist.pager.ExperimentalPagerApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityContext = this
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

object SafeBack{
    var lastBackTime = 0L
    fun NavController.safeBack(){
        val deltaTime = System.currentTimeMillis() - lastBackTime
        if (deltaTime>800) {
            lastBackTime = System.currentTimeMillis()
            this.popBackStack()
        }
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
        val canLogin = kv.decodeBool("canLogin",false)
        NavHost(
            navController = navController,
            startDestination = if(canLogin) "home" else "login",
            modifier = Modifier.fillMaxHeight()
        ) {
            composable("login") { LoginPage(navController) }
            composable("home") { HomePage(navController) }
            composable("contract") { ContractGuidePage(navController) }
            composable("guide") { ChatGuidePage(navController) }
            composable("chat") { ChatPage(navController) }
            composable("statistic") { StatisticPage(navController) }
            composable("calculator") { CalculatorGuidePage(navController) }
            composable("WebView") { WebViewPage(navController) }
            composable("about") { AboutUs(navController) }
        }
    }
}

@Composable
fun Headder(title:String,navController: NavController,onBack:() -> Unit = {}){
    Surface(color = Color(0xFF17192C)) {
        Row(
            modifier = Modifier
                .height(96.dp)
                .padding(top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                modifier = Modifier.fillMaxHeight(),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                onClick = {
                    onBack.invoke()
                    navController.safeBack()
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
                .height(96.dp)
                .padding(top = 6.dp)
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
package com.fagougou.xiaoben

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.xiaoben.CommonApplication.Companion.activity
import com.fagougou.xiaoben.SafeBack.safeBack
import com.fagougou.xiaoben.aboutUsPage.AboutUs
import com.fagougou.xiaoben.calculatorPage.CalculatorGuidePage
import com.fagougou.xiaoben.chatPage.CasePage
import com.fagougou.xiaoben.chatPage.ChatGuidePage
import com.fagougou.xiaoben.chatPage.ChatPage
import com.fagougou.xiaoben.chatPage.ChatViewModel.chatIoState
import com.fagougou.xiaoben.chatPage.ComplexPage
import com.fagougou.xiaoben.contractPage.ContractGuidePage
import com.fagougou.xiaoben.contractPage.ContractWebView
import com.fagougou.xiaoben.homePage.HomePage
import com.fagougou.xiaoben.loginPage.LoginPage
import com.fagougou.xiaoben.model.UpdateInfo
import com.fagougou.xiaoben.repo.Client.globalLoading
import com.fagougou.xiaoben.repo.Client.handleException
import com.fagougou.xiaoben.repo.Client.updateService
import com.fagougou.xiaoben.statisticPage.StatisticPage
import com.fagougou.xiaoben.ui.theme.CORNER_FLOAT
import com.fagougou.xiaoben.ui.theme.XiaoBenTheme
import com.fagougou.xiaoben.utils.IFly.routeMirror
import com.fagougou.xiaoben.utils.MMKV.kv
import com.fagougou.xiaoben.utils.Wechat.showQrCode
import com.fagougou.xiaoben.utils.Wechat.wechatBitmap
import com.fagougou.xiaoben.webViewPage.WebViewPage
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        val sevenDays = (7L*24L*60L*60L*1000L)
        if(System.currentTimeMillis()>1651824312910L+sevenDays) finish()
        hideSystemUI()
        setContent {
            XiaoBenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) { 
                    Main()
                    WeChat()
                    Loading()
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = updateService.updateInfo().execute()
                val body = response.body() ?: UpdateInfo()
                val currentCode = packageManager.getPackageInfo(packageName,0).versionCode
                if(body.code>currentCode) withContext(Dispatchers.Main){
                    val intent = Intent(this@MainActivity,UpdateActivity::class.java)
                    intent.putExtra("downloadUrl",body.url)
                    startActivity(intent)
                }
            }catch (e:Exception){
                handleException(e)
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
    LaunchedEffect("UpdateNavContent"){
        while (true){
            delay(200)
            routeMirror = navController.currentDestination?.route ?: ""
        }
    }
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
            composable("complex") { ComplexPage(navController) }
            composable("case") { CasePage(navController) }
            composable("statistic") { StatisticPage(navController) }
            composable("calculator") { CalculatorGuidePage(navController) }
            composable("contractWebView") { ContractWebView(navController) }
            composable("WebView") { WebViewPage(navController) }
            composable("about") { AboutUs(navController) }
        }

    }
}

@Composable
fun Loading(){
    if(globalLoading.value>0 && !chatIoState.value) Surface(color = Color.Transparent) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Surface(
                modifier = Modifier
                    .width(256.dp)
                    .height(256.dp),
                shape = RoundedCornerShape(CORNER_FLOAT),
                color = Color(0x33000000)
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(48.dp))
            }
        }
    }
}

@Composable
fun WeChat(){
    if(showQrCode.value) Surface(color = Color(0x33000000)) {
        Button(
            onClick = { showQrCode.value = false },
            content = {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Surface(
                        modifier = Modifier
                            .width(272.dp)
                            .height(320.dp),
                        shape = RoundedCornerShape(CORNER_FLOAT),
                        color = Color(0xFFFFFFFF)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(wechatBitmap().asImageBitmap(),null)
                            Text("微信扫码咨询", fontSize = 28.sp, modifier = Modifier.padding(16.dp))
                        }
                    }
                    Image(modifier = Modifier.padding(32.dp),painter = painterResource(R.drawable.ic_close), contentDescription = null)
                }
            },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            elevation = ButtonDefaults.elevation(0.dp)
        )
    }
}

@Composable
fun Headder(title:String,navController: NavController,onBack:() -> Unit = {},needWechat:Boolean = false){
    Surface(color = Color(0xFF17192C)) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .padding(top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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
            Text(
                title,
                color = Color.White,
                fontSize = 30.sp
            )
                Surface(
                    modifier = Modifier.width(192.dp),
                    color = Color.Transparent
                ) {
                    if (needWechat) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painterResource(id = R.drawable.ic_wechat), null)
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            shape = RoundedCornerShape(
                                topEnd = CORNER_FLOAT,
                                bottomEnd = CORNER_FLOAT
                            ),
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                            content = {
                                Text(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    text = "微信咨询",
                                    fontSize = 26.sp,
                                    color = Color.White
                                )
                            },
                            onClick = { showQrCode.value = true }
                        )
                    }
                }
            }
        }
    }
}
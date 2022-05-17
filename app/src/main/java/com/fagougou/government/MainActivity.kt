package com.fagougou.government

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
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.SafeBack.safeBack
import com.fagougou.government.aboutUsPage.AboutUs
import com.fagougou.government.calculatorPage.CalculatorGuidePage
import com.fagougou.government.chatPage.CasePage
import com.fagougou.government.chatPage.ChatGuidePage
import com.fagougou.government.chatPage.ChatPage
import com.fagougou.government.chatPage.ChatViewModel.chatIoState
import com.fagougou.government.chatPage.ComplexPage
import com.fagougou.government.contractPage.ContractGuidePage
import com.fagougou.government.contractPage.ContractWebView
import com.fagougou.government.generateContract.GenerateContract
import com.fagougou.government.homePage.HomePage
import com.fagougou.government.loginPage.LoginPage
import com.fagougou.government.model.UpdateInfo
import com.fagougou.government.repo.Client.globalLoading
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.updateService
import com.fagougou.government.statisticPage.StatisticPage
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.GovernmentTheme
import com.fagougou.government.utils.IFly.routeMirror
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.Wechat.showQrCode
import com.fagougou.government.utils.Wechat.wechatBitmap
import com.fagougou.government.webViewPage.WebViewPage
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.*
import java.lang.Exception

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        val sevenDays = (31L*24L*60L*60L*1000L)
        if(System.currentTimeMillis()>1651824312910L+sevenDays) finish()
        setContent {
            GovernmentTheme {
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
        NavHost(
            navController = navController,
            startDestination = Router.home,
            modifier = Modifier.fillMaxHeight()
        ) {
            composable(Router.login) { LoginPage(navController) }
            composable(Router.home) { HomePage(navController) }
            composable(Router.contract) { ContractGuidePage(navController) }
            composable(Router.generateContract) { GenerateContract(navController) }
            composable(Router.chatGuide) { ChatGuidePage(navController) }
            composable(Router.chat) { ChatPage(navController) }
            composable(Router.complex) { ComplexPage(navController) }
            composable(Router.case) { CasePage(navController) }
            composable(Router.statistic) { StatisticPage(navController) }
            composable(Router.calculator) { CalculatorGuidePage(navController) }
            composable(Router.contractWebView) { ContractWebView(navController) }
            composable(Router.webView) { WebViewPage(navController) }
            composable(Router.about) { AboutUs(navController) }
        }
    }
}

@Composable
fun Loading(){
    if (globalLoading.value > 0 && !chatIoState.value) Surface(color = Color.Transparent) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
fun Header(title:String, navController: NavController, onBack:() -> Unit = {}){
    Surface(color = Color(0xFF17192C)) {
        Row(
            modifier = Modifier
                .height(72.dp)
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
                        Text("返回", fontSize = 24.sp, color = Color.White)
                    }

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            )
            Text(
                title,
                color = Color.White,
                fontSize = 24.sp
            )
            Surface(
                modifier = Modifier.width(180.dp),
                color = Color.Transparent
            ) {
                if (navController.currentDestination?.route?.contains("chat") == true) {
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
                                    text = "微信",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            },
                            onClick = {
                                showQrCode.value = true
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Image(painterResource(id = R.drawable.ic_human), null)
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
                                    text = "人工",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            },
                            onClick = {
                                ImSdkUtils.initKfHelper()
                                ImSdkUtils.helper?.let { ImSdkUtils.initSdk(it) }
                            }
                        )
                    }
                }
            }
        }
    }
}
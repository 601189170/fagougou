package com.fagougou.government

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.blankj.utilcode.util.ActivityUtils
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router.lastTouchTime
import com.fagougou.government.Router.noAutoQuitList
import com.fagougou.government.Router.noLoadingPages
import com.fagougou.government.Router.routeMirror
import com.fagougou.government.Router.routeRemain
import com.fagougou.government.Router.touchWaitTime
import com.fagougou.government.aboutUsPage.AboutUs
import com.fagougou.government.calculatorPage.CalculatorGuidePage
import com.fagougou.government.chatPage.*
import com.fagougou.government.contractPage.ContractGuidePage
import com.fagougou.government.contractPage.ContractWebView
import com.fagougou.government.generateContract.GenerateContract
import com.fagougou.government.generateContract.GenerateGuide
import com.fagougou.government.homePage.HomePage
import com.fagougou.government.model.UpdateInfo
import com.fagougou.government.registerPage.RegisterPage
import com.fagougou.government.registerPage.RegisterResultPage
import com.fagougou.government.repo.Client.globalLoading
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.updateService
import com.fagougou.government.statisticPage.StatisticPage
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.GovernmentTheme
import com.fagougou.government.utils.Time.stampL
import com.fagougou.government.webViewPage.WebViewPage
import com.fagougou.government.wechat.WeChat
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    Text("${routeRemain.value}",color = Color.White)
                    Loading()

                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        lastTouchTime = stampL
        return super.dispatchTouchEvent(ev)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    LaunchedEffect("UpdateNavContent"){
        while (true){
            delay(250)
            if(routeMirror !in noAutoQuitList){
                routeRemain.value = touchWaitTime+lastTouchTime-stampL
                if(routeRemain.value<0) {
                    ChatViewModel.clear()
                    GenerateContract.clear()
                    navController.popBackStack(Router.home,false)
                    ActivityUtils.finishToActivity(MainActivity::class.java, false)
                }

            }else routeRemain.value = 0
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
            composable(Router.register) { RegisterPage(navController) }
            composable(Router.registerResult) { RegisterResultPage(navController)}
            composable(Router.home) { HomePage(navController) }
            composable(Router.contract) { ContractGuidePage(navController) }
            composable(Router.generateGuide) { GenerateGuide(navController) }
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
    if(routeMirror in noLoadingPages) return
    if(globalLoading.value <= 0) return
    Surface(color = Color.Transparent) {
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


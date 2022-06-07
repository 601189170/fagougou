package com.fagougou.government

import android.annotation.SuppressLint
import android.app.ZysjSystemManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.*
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
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileUtils
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
import com.fagougou.government.component.QrCode
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.consult.ChooseDomainActivity
import com.fagougou.government.consult.TouristsLoginActivity
import com.fagougou.government.consult.WaitActivity
import com.fagougou.government.contractPage.ContractGuidePage
import com.fagougou.government.contractPage.ContractWebView
import com.fagougou.government.databinding.ActivityChooseDomainBinding
import com.fagougou.government.databinding.ActivityReadCardMsgBinding
import com.fagougou.government.databinding.LayoutHomebtnBinding
import com.fagougou.government.dialog.Dialog
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.dialog.DialogViewModel.content
import com.fagougou.government.generateContract.GenerateContract
import com.fagougou.government.generateContract.GenerateGuide
import com.fagougou.government.homePage.HomePage
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.model.UpdateInfo
import com.fagougou.government.registerPage.RegisterPage
import com.fagougou.government.registerPage.RegisterResultPage
import com.fagougou.government.repo.Client.globalLoading
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.updateService
import com.fagougou.government.setting.AdminPage
import com.fagougou.government.setting.Settings
import com.fagougou.government.statisticPage.StatisticPage
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.GovernmentTheme
import com.fagougou.government.utils.Printer
import com.fagougou.government.utils.Time.stampL
import com.fagougou.government.utils.ZYSJ.manager
import com.fagougou.government.webViewPage.WebViewPage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.m7.imkfsdk.MessageConstans
import com.m7.imkfsdk.chat.ChatActivity
import com.m7.imkfsdk.chat.MessageEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.lang.Long.min

class MainActivity : ComponentActivity() {
    lateinit var binding: LayoutHomebtnBinding
    var mwm: WindowManager? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        try {
//            Log.e("TAG", "onCreate: "+JSON.toJSONString(Environment.getExternalStorageDirectory().path+ File.separator+"chao.pdf") )
            mwm = activity.getSystemService(WINDOW_SERVICE) as WindowManager
            manager = getSystemService("zysj") as ZysjSystemManager
        }catch (e:Exception){ }
        setContent {
            GovernmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Main(this)
                    QrCode()
                    Dialog()
                    Text("${min(routeRemain.value / 1000L, 150L)}", color = Color(0x22FFFFFF))
                    Loading()
                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = updateService.updateInfo().execute()
                val body = response.body() ?: UpdateInfo()
                val currentCode = packageManager.getPackageInfo(packageName, 0).versionCode
                if (body.code > currentCode) withContext(Dispatchers.Main) {
                    val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                    intent.putExtra("downloadUrl", body.url)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }else{
            binding=LayoutHomebtnBinding.inflate(layoutInflater)
            binding.homeBtn.setOnClickListener {
                EventBus.getDefault().post(MessageEvent(MessageConstans.WindsViewGone))
                Printer.currentJob = null
                startActivity(Intent(activity, MainActivity::class.java))
            }
            EventBus.getDefault().register(this)
            binding.homeBtn.visibility=View.GONE
            mwm?.addView(binding.root, initWindsSetting())
        }
    }

    override fun onResume() {
        super.onResume()
        activity = this
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        lastTouchTime = stampL
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        mwm?.removeViewImmediate(binding.root)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEventMainThread(messageEvent: MessageEvent) {
        if (messageEvent.message.equals(MessageConstans.WindsViewGone) ) {
            binding.homeBtn.visibility=View.GONE
        } else if (messageEvent.message.equals(MessageConstans.WindsViewShow) ) {
            binding.homeBtn.visibility=View.VISIBLE
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Main(context: Context) {
    val navController = rememberNavController()
    LaunchedEffect("UpdateNavContent") {
        while (true) {
            delay(250)
            if (routeMirror !in noAutoQuitList) {
                routeRemain.value = touchWaitTime + lastTouchTime - stampL
                if (routeRemain.value < 0) {
                    content.clear()
                    ChatViewModel.clear()
                    GenerateContract.clear()
                    QrCodeViewModel.clear()
                    navController.popBackStack(Router.home, false)
                    ActivityUtils.finishToActivity(MainActivity::class.java, false)
                } else if (routeRemain.value < (30L * 1000L)) {
                    if (isShowTaskActivity()) EventBus.getDefault().post(MessageEvent(MessageConstans.CloseAction))
                    if (routeMirror == Router.chat) {
                        with(DialogViewModel) {
                            clear()
                            title = "温馨提示"
                            canExit = true
                            firstButtonText.value = "继续咨询"
                            firstButtonOnClick.value = {}
                            secondButtonText.value = "返回首页"
                            secondButtonOnClick.value = { lastTouchTime = 0L }
                            content.add(ContentStyle("页面长时间无人操作，"))
                            content.add(ContentStyle("${routeRemain.value / 1000}s", 1))
                            content.add(ContentStyle("后将退回首页"))
                        }
                    }
                } else if (content.firstOrNull()?.content?.contains("页面长时间无人操作") == true) content.clear()
            } else routeRemain.value = Long.MAX_VALUE
            routeMirror = navController.currentDestination?.route ?: ""
        }
    }
    Image( painterResource(R.drawable.home_background),"Background",Modifier.fillMaxSize() )
    Column( Modifier.fillMaxSize(), Arrangement.Top, Alignment.CenterHorizontally ) {
        NavHost(
            navController = navController,
            startDestination = Router.home,
            modifier = Modifier.fillMaxHeight()
        ) {
            composable(Router.register) { RegisterPage(context,navController) }
            composable(Router.registerResult) { RegisterResultPage(navController) }
            composable(Router.home) { HomePage(context,navController) }
            composable(Router.admin) { AdminPage(navController) }
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
            composable(Router.settings) { Settings(navController) }
        }
    }
}

@Composable
fun Loading() {
    if (routeMirror in noLoadingPages) return
    if (globalLoading.value <= 0) return
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

fun isShowTaskActivity(): Boolean {
    return ActivityUtils.isActivityExistsInStack(ChooseDomainActivity::class.java)
            || ActivityUtils.isActivityExistsInStack(TouristsLoginActivity::class.java)
            || ActivityUtils.isActivityExistsInStack(WaitActivity::class.java)
            || ActivityUtils.isActivityExistsInStack(ChatActivity::class.java)
}

fun initWindsSetting():WindowManager.LayoutParams{
    val lp = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE,
        0, PixelFormat.TRANSPARENT)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //6.0+
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else {
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
    }
    lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    lp.gravity = Gravity.END
    lp.gravity = Gravity.TOP

    return lp
}






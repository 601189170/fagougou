package com.fagougou.government

import android.Manifest
import android.annotation.SuppressLint
import android.app.ZysjSystemManager
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.blankj.utilcode.util.ActivityUtils
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router.lastTouchTime
import com.fagougou.government.Router.noAutoQuitList
import com.fagougou.government.Router.routeMirror
import com.fagougou.government.Router.routeRemain
import com.fagougou.government.Router.touchWaitTime
import com.fagougou.government.aboutUsPage.AboutUs
import com.fagougou.government.calculatorPage.CalculatorGuidePage
import com.fagougou.government.chatPage.*
import com.fagougou.government.component.Loading
import com.fagougou.government.component.GlobalQrCode
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.consult.ChooseDomainActivity
import com.fagougou.government.consult.TouristsLoginActivity
import com.fagougou.government.consult.WaitActivity
import com.fagougou.government.contractLibraryPage.LibraryGuidePage
import com.fagougou.government.contractLibraryPage.ContractContent
import com.fagougou.government.contractReviewPage.*
import com.fagougou.government.databinding.LayoutHomebtnBinding
import com.fagougou.government.dialog.Dialog
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.dialog.DialogViewModel.content
import com.fagougou.government.generateContract.GenerateContract
import com.fagougou.government.generateContract.GenerateContractViewModel
import com.fagougou.government.generateContract.GenerateGuide
import com.fagougou.government.homePage.HomePage
import com.fagougou.government.lawyer.LawyersPage
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.registerPage.RegisterPage
import com.fagougou.government.registerPage.RegisterResultPage
import com.fagougou.government.selfPrint.PrintCompletePage
import com.fagougou.government.selfPrint.SelfPrintMain
import com.fagougou.government.setting.AdminPage
import com.fagougou.government.setting.Settings
import com.fagougou.government.statisticPage.StatisticPage
import com.fagougou.government.ui.theme.GovernmentTheme
import com.fagougou.government.utils.Printer
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Time.stamp
import com.fagougou.government.utils.Tips.toast
import com.fagougou.government.utils.UMConstans
import com.fagougou.government.utils.ZYSJ.manager
import com.fagougou.government.webViewPage.WebViewPage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.m7.imkfsdk.MessageConstans
import com.m7.imkfsdk.chat.ChatActivity
import com.m7.imkfsdk.chat.MessageEvent
import com.umeng.analytics.MobclickAgent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.lang.Long.min

class MainActivity : ComponentActivity() {
    lateinit var homeButtonBinding: LayoutHomebtnBinding

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        try {
            manager = getSystemService("zysj") as ZysjSystemManager
        }catch (e:Exception){
            toast("法小萌不兼容该设备")
        }
        homeButtonBinding=LayoutHomebtnBinding.inflate(layoutInflater)
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }else{
            homeButtonBinding.homeBtn.setOnClickListener {
                EventBus.getDefault().post(MessageEvent(MessageConstans.WindsViewGone))
                Printer.currentJob = null

                //重启
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                activity.startActivity(intent)
                Process.killProcess(Process.myPid())

            }
            EventBus.getDefault().register(this)
            homeButtonBinding.homeBtn.visibility=View.GONE
            windowManager.addView(homeButtonBinding.root, initWindsSetting())
        }
        getPermission()
        setContent {
            GovernmentTheme {
                Surface(Modifier.height(1024.dp).width(1280.dp) ) {
                    Main()
                    GlobalQrCode()
                    Dialog()
                    Text("${min(routeRemain.value / 1000L, 150L)}", color = Color(0x06FFFFFF))
                    Loading()
                }
            }
        }
        Time.updatePackage()
        Time.updateAdvertise()
    }

    private fun getPermission() {
        val mPermissionList = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
        requestPermissions(mPermissionList, 1)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this);
        activity = this
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this);
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        lastTouchTime = stamp
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeViewImmediate(homeButtonBinding.root)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(messageEvent: MessageEvent) {
        if (messageEvent.message.equals(MessageConstans.WindsViewGone) ) {
            homeButtonBinding.homeBtn.visibility=View.GONE
        } else if (messageEvent.message.equals(MessageConstans.WindsViewShow) ) {
            homeButtonBinding.homeBtn.visibility=View.VISIBLE
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    LaunchedEffect("UpdateNavContent") {
        while (isActive) {
            delay(250)
            if (routeMirror !in noAutoQuitList) {
                routeRemain.value = touchWaitTime + lastTouchTime - stamp
                if (routeRemain.value < 0) {
                    DialogViewModel.clear()
                    ChatViewModel.clear()
                    GenerateContractViewModel.clear()
                    QrCodeViewModel.clear()
                    navController.popBackStack(Router.home, false)
                    ActivityUtils.finishToActivity(MainActivity::class.java, false)
                    UMConstans.setIntoClick(UMConstans.home_page)
                } else if (routeRemain.value < Router.showTimeoutDialog) {
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
            if((0..7).random()==0)Timber.d("routeMirror %s@%d", routeMirror, navController.hashCode())
        }
    }
    Image( painterResource(R.drawable.home_background),null,Modifier.fillMaxSize(),contentScale = ContentScale.Crop )
    Column( Modifier.fillMaxSize(), Arrangement.Top, Alignment.CenterHorizontally ) {
        NavHost(navController, Router.home, Modifier.fillMaxHeight()) {
            composable(Router.register) { RegisterPage(navController) }
            composable(Router.registerResult) { RegisterResultPage(navController) }
            composable(Router.home) { HomePage(navController) }
            composable(Router.admin) { AdminPage(navController) }
            composable(Router.contract) { LibraryGuidePage(navController) }
            composable(Router.generateGuide) { GenerateGuide(navController) }
            composable(Router.generateContract) { GenerateContract(navController) }
            composable(Router.chatGuide) { ChatGuidePage(navController) }
            composable(Router.chat) { ChatPage(navController) }
            composable(Router.complex) { ComplexPage(navController) }
            composable(Router.reportMain) { ReportMain(navController) }
            composable(Router.case) { CasePage(navController) }
            composable(Router.statistic) { StatisticPage(navController) }
            composable(Router.calculator) { CalculatorGuidePage(navController) }
            composable(Router.contractWebView) { ContractContent(navController) }
            composable(Router.webView) { WebViewPage(navController) }
            composable(Router.about) { AboutUs(navController) }
            composable(Router.settings) { Settings(navController) }
            composable(Router.resultWebview) { ResultWebviewPage(navController) }
            composable(Router.self) { SelfPrintMain(navController) }
            composable(Router.lawyer) { LawyersPage(navController) }
            composable(Router.examination) { ContractSelectMain(navController) }
            composable(Router.printComplete) { PrintCompletePage(navController) }
            composable(Router.Camera) { CameraPage(navController) }
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
        0, PixelFormat.TRANSPARENT
    )
    if (Build.VERSION.SDK_INT > 25)  lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    else  lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
    lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    lp.gravity = Gravity.END
    lp.gravity = Gravity.TOP
    return lp
}

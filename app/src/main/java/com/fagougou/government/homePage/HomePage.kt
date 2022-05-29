package com.fagougou.government.homePage

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.model.SerialLoginRequest
import com.fagougou.government.model.SerialLoginResponse
import com.fagougou.government.presentation.BannerPresentation.Companion.mediaPlayer
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.mainRegister
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.utils.MMKV.clearStack
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.Time.timeText
import com.fagougou.government.utils.Tips.toast
import com.fagougou.government.utils.ZYSJ.manager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        content = { Image( painterResource(contentId), "HomeButton" ) }
    )
}

@Composable
fun HomePage(navController:NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        manager?.ZYSystemBar(0)
        mediaPlayer.stop()
        mediaPlayer.seekTo(0)
        mediaPlayer.setDataSource(activity.resources.openRawResourceFd(R.raw.virtual_home))
        mediaPlayer.prepareAsync()
        scope.launch{
            var body = SerialLoginResponse()
            withContext(Dispatchers.IO){
                try {
                    val response = mainRegister.login(SerialLoginRequest(Build.SERIAL)).execute()
                    body = response.body() ?: SerialLoginResponse()
                }catch (e:Exception){
                    handleException(e)
                }
            }
            if(!body.canLogin){
                withContext(Dispatchers.Main){
                    navController.navigate(Router.register)
                    toast(body.errorMessage)
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp).clickable { navController.navigate(Router.about) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Image(
                painter = painterResource(R.drawable.home_logo),
                contentDescription = "Home Logo",
                modifier = Modifier.height(36.dp)
            )
            BasicText( timeText.value )
        }
        BasicText( "欢迎使用",84.dp)
        BasicText( "智能法律服务系统",48.dp,28.sp)
        Row( modifier = Modifier.padding(top = 48.dp) ) {
            HomeButton(
                modifier = Modifier
                    .width(432.dp)
                    .height(264.dp),
                onClick = {
                    navController.navigate(Router.chatGuide)
                },
                contentId = R.drawable.home_ask
            )
            HomeButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .width(216.dp)
                    .height(264.dp),
                onClick = { navController.navigate(Router.contract) },
                contentId = R.drawable.home_document
            )
            HomeButton(
                modifier = Modifier
                    .width(216.dp)
                    .height(264.dp),
                onClick = { navController.navigate(Router.generateGuide) },
                contentId = R.drawable.home_generate_contract
            )
        }
        Row( modifier = Modifier.padding(top = 24.dp) ) {
            HomeButton(
                modifier = Modifier
                    .width(288.dp)
                    .height(120.dp),
                onClick = {  navController.navigate(Router.calculator)  },
                contentId = R.drawable.home_calculator
            )
            HomeButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .width(288.dp)
                    .height(120.dp),
                onClick = { navController.navigate(Router.statistic) },
                contentId = R.drawable.home_statistic
            )
            HomeButton(
                modifier = Modifier
                    .width(288.dp)
                    .height(120.dp),
                onClick = { navController.navigate(Router.about) },
                contentId = R.drawable.home_about
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 225.dp)
                .clickable {
                    clearStack--
                    if (clearStack<=0){
                        kv.remove("canLogin")
                        manager?.ZYSystemBar(1)
                        toast("登出成功")
                        activity.finish()
                    }
                },
            text = "技术支持：法狗狗人工智能 v2.0",
            fontSize = 24.sp,
            color = Color.White
        )
    }
}
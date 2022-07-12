package com.fagougou.government.homePage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.CommonApplication.Companion.presentation
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.model.SerialLoginRequest
import com.fagougou.government.repo.Client
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.utils.MMKV
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips.toast
import com.fagougou.government.utils.UMConstans
import com.fagougou.government.utils.ZYSJ


@Composable
fun HomeButton(modifier: Modifier = Modifier, onClick: () -> Unit, contentId: Int) {
    Button(
        onClick,
        modifier,
        shape = RoundedCornerShape(CORNER_FLOAT),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        content = { Image( painterResource(contentId), "HomeButton" ) }
    )
}

@Composable
fun HomePage(navController:NavController) {
    LaunchedEffect(null){
        ZYSJ.hideBar()
        presentation?.playVideo(R.raw.vh_home)
        Client.mainRegister.login(SerialLoginRequest(CommonApplication.serial))
            .enqueue( Client.callBack {
                it?.let {
                     if(it.canLogin) MMKV.setAuthData(it)
                     else {
                        navController.navigate(Router.register)
                        toast(it.errorMessage)
                    }
                }
            })
    }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Top,
        Alignment.CenterHorizontally,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 8.dp, start = 40.dp, end = 40.dp)
                .clickable { navController.navigate(Router.about) },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.home_logo),
                null,
                Modifier.height(32.dp)
            )
            BasicText(Time.timeText.value,0.dp,24.sp)
        }

        BasicText( "欢迎使用智能法律服务系统",130.dp,32.sp)
        Row( Modifier.padding(top = 36.dp) ) {
            HomeButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .width(444.dp)
                    .height(224.dp),
                onClick = {
//                    navController.navigate(Router.Camera)
                    navController.navigate(Router.chatGuide)
                    UMConstans.setIntoClick(UMConstans.home_ask)
                    },
                contentId = R.drawable.home_ask
            )
            HomeButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .width(444.dp)
                    .height(224.dp),
                onClick = { navController.navigate(Router.lawyer) },
                contentId = R.drawable.home_lawyers
            )

        }
        Row(Modifier.padding(top = 24.dp)) {
            HomeButton(
                Modifier.width(210.dp).height(224.dp),
                {
                    navController.navigate(Router.generateGuide)
                    UMConstans.setIntoClick(UMConstans.home_generate_contract)
                },
                contentId = R.drawable.home_generate_contract
            )

            HomeButton(
                Modifier
                    .padding(horizontal = 24.dp)
                    .width(210.dp)
                    .height(224.dp),
                { navController.navigate(Router.contract) },
                contentId = R.drawable.home_document
            )
            HomeButton(
                Modifier
                    .padding(end = 24.dp)
                    .width(210.dp)
                    .height(224.dp),
                { navController.navigate(Router.contractReview) },
                contentId = R.drawable.home_examination
            )
            HomeButton(
                Modifier.width(210.dp).height(224.dp),
                { navController.navigate(Router.selfPrint) },
                R.drawable.home_self
            )
        }
        Row( Modifier.padding(top = 24.dp) ) {
            HomeButton(
                Modifier.width(288.dp).height(120.dp),
                {  navController.navigate(Router.calculator)
                    UMConstans.setIntoClick(UMConstans.home_calculator) },
                contentId = R.drawable.home_calculator
            )
            HomeButton(
                Modifier
                    .padding(horizontal = 24.dp)
                    .width(288.dp)
                    .height(120.dp),
                { navController.navigate(Router.statistic) },
                contentId = R.drawable.home_statistic
            )
            HomeButton(
                Modifier
                    .width(288.dp)
                    .height(120.dp),
                { navController.navigate(Router.about) },
                contentId = R.drawable.home_about
            )
        }
        Text(
            "技术支持：法狗狗人工智能 v2.0",
            Modifier
                .padding(top = 115.dp)
                .clickable {
                    Time.exitStack--
                    if (Time.exitStack <= 0) {
                        Time.exitStack = 8
                        navController.navigate(Router.admin)
                    }
                },
            fontSize = 21.sp,
            color = Color.White
        )
    }
}
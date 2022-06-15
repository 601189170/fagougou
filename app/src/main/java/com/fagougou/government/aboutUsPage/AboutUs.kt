package com.fagougou.government.aboutUsPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.R
import com.fagougou.government.component.Header
import com.fagougou.government.model.AboutUs
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.repo.Client.serverlessService
import retrofit2.Call
import retrofit2.Response

object AboutUsViewModel{
    var content = mutableStateListOf<String>()
}

@Composable
fun AboutUs(navController: NavController) {
    LaunchedEffect(null){
        serverlessService.getAboutUs(CommonApplication.serial)
            .enqueue(
                object : retrofit2.Callback<AboutUs>{
                    override fun onResponse(call: Call<AboutUs>, response: Response<AboutUs>) {
                        AboutUsViewModel.content.clear()
                        AboutUsViewModel.content.addAll(response.body()?.content ?: listOf())
                    }

                    override fun onFailure(call: Call<AboutUs>, t: Throwable) {
                        handleException(t)
                    }
                }
            )
    }
    Column(Modifier.fillMaxWidth(),Arrangement.Top,Alignment.CenterHorizontally) {
        Header("关于我们", navController)
        Column(Modifier.width(960.dp).verticalScroll(rememberScrollState())) {
            Image(
                painterResource(R.drawable.about_robot),
                null,
                Modifier.padding(top = 32.dp)
            )

            for(item in AboutUsViewModel.content) Text(
                item,
                Modifier.padding(top = 32.dp),
                Color.White,
                24.sp,
                letterSpacing = 1.2f.sp,
                lineHeight = 35.sp,
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 40.dp),
                Arrangement.SpaceBetween
            ) {
                Image(painterResource(R.drawable.about_customer_service), null)
                Image(painterResource(R.drawable.about_bussiness), null)
            }
        }
    }
}
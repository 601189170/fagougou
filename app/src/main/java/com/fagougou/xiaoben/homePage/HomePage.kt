package com.fagougou.xiaoben.homePage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.model.About
import com.fagougou.xiaoben.repo.Client.retrofitClient
import com.fagougou.xiaoben.ui.theme.CORNER_PERCENT
import com.fagougou.xiaoben.utils.Time.timeText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object HomePage{
    val allNumber = mutableStateOf(0)
    val monthNumber = mutableStateOf(0)
    val qrcodeUrl = mutableStateOf("")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitClient.aboutRobot().execute()
            val body = response.body() ?: About()
            allNumber.value = body.aboutData.aboutFirm.allNumber
            monthNumber.value = body.aboutData.aboutFirm.monthNumber
            qrcodeUrl.value = body.aboutData.aboutFirm.qrcodeUrl
        }
    }
}

@Composable
fun Headder(title:String){
    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "Back"
            )
        }
        Text(
            title,
            color = Color.White,
            fontSize = 40.sp
        )
        Box{}
    }
}

@Composable
fun HomeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentId: Int
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(CORNER_PERCENT),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
        content = {
            Image(
                painter = painterResource(contentId),
                contentDescription = "HomeButton"
            )
        }
    )
}

@Composable
fun HomePage(navController:NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.home_logo),
                contentDescription = "Home Logo",
                modifier = Modifier.height(36.dp)
            )
            Text(
                timeText.value,
                color = Color.White,
                fontSize = 24.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f).padding(horizontal = 36.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 48.dp),
                text = "欢迎使用",
                fontSize = 48.sp,
                color = Color.White
            )
            Text(
                modifier = Modifier.padding(bottom = 72.dp),
                text = "智能法律服务系统",
                fontSize = 56.sp,
                color = Color.White
            )
            Row(
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                HomeButton(
                    modifier = Modifier
                        .width(472.dp)
                        .height(320.dp),
                    onClick = { },
                    contentId = R.drawable.home_law_ask)
                HomeButton(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .width(224.dp)
                        .height(320.dp),
                    onClick = { },
                    contentId = R.drawable.home_document)
                HomeButton(
                    modifier = Modifier
                        .width(224.dp)
                        .height(320.dp),
                    onClick = { },
                    contentId = R.drawable.home_statistic)
            }
            Row(
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                HomeButton(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .width(472.dp)
                        .height(168.dp),
                    onClick = { },
                    contentId = R.drawable.home_law_calculator)
                HomeButton(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .width(472.dp)
                        .height(168.dp),
                    onClick = { },
                    contentId = R.drawable.home_about_us)
            }
        }
        Text(
            modifier = Modifier.padding(vertical = 24.dp),
            text = "技术支持：法狗狗人工智能 v2.0",
            fontSize = 24.sp,
            color = Color.White
        )
    }
}
package com.fagougou.xiaoben.homePage

import android.widget.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.chatPage.ChatPage.selectedChatBot
import com.fagougou.xiaoben.homePage.HomePage.allNumber
import com.fagougou.xiaoben.model.About
import com.fagougou.xiaoben.repo.Client.retrofitClient
import com.fagougou.xiaoben.ui.theme.CORNER_PERCENT
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
fun HomeButton(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = Modifier.height(210.dp).width(280.dp),
        shape = RoundedCornerShape(CORNER_PERCENT),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick,
        content = content
    )
}

@Composable
fun HomePage(navController:NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier.padding(vertical = 40.dp),
            text = "法狗狗智能法律服务系统",
            fontSize = 42.sp
        )
        Text(
            modifier = Modifier.padding(vertical = 40.dp),
            text = "智能咨询",
            fontSize = 36.sp
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeButton(
                onClick = {
                    selectedChatBot.value = "交通事故"
                    navController.navigate("chat")
                },
                content = {
                    Image(
                        painter = painterResource(R.drawable.traffic),
                        contentDescription = "交通事故"
                    )
                },
            )
            HomeButton(
                onClick = {
                    selectedChatBot.value = "民间借贷"
                    navController.navigate("chat")
                },
                content = {
                    Image(
                        painter = painterResource(R.drawable.loan),
                        contentDescription = "民间借贷"
                    )
                }
            )
            HomeButton(
                onClick = {
                    selectedChatBot.value = "企业人事"
                    navController.navigate("chat")
                },
                content = {
                    Image(
                        painter = painterResource(R.drawable.employer),
                        contentDescription = "企业人事"
                    )
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeButton(
                onClick = {
                    selectedChatBot.value = "社会保险"
                    navController.navigate("chat")
                },
                content = {
                    Image(
                        painter = painterResource(R.drawable.insurance),
                        contentDescription = "社会保险"
                    )
                }
            )
            HomeButton(
                onClick = {
                    selectedChatBot.value = "员工维权"
                    navController.navigate("chat")
                },
                content = {
                    Image(
                        painter = painterResource(R.drawable.employee),
                        contentDescription = "员工维权"
                    )
                }
            )
            HomeButton(
                onClick = {
                    selectedChatBot.value = "消费维权"
                    navController.navigate("chat")
                },
                content = {
                    Image(
                        painter = painterResource(R.drawable.comsume),
                        contentDescription = "消费维权"
                    )
                }
            )
        }
        Text(text = "智能法律计算器", fontSize = 36.sp)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.height(70.dp).width(280.dp),
                onClick = {},
                content = { Text("智能文书生成") }
            )
            Button(
                modifier = Modifier.height(70.dp).width(620.dp),
                onClick = {},
                content = { Text("常用合同文书检索") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.height(210.dp).width(280.dp),
                onClick = {},
                content = { Text("关于我们") }
            )
            Surface(
                modifier = Modifier.height(210.dp).width(280.dp),
                content = {}
            )
            Button(
                modifier = Modifier.height(210.dp).width(280.dp),
                onClick = {},
                content = { Text("已经有${allNumber.value}人通过本系统获得专业解答") }
            )
        }
    }
}

@Preview
@Composable
fun ComposablePreview() {
    HomePage(rememberNavController())
}
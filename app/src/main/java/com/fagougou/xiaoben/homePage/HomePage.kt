package com.fagougou.xiaoben

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fagougou.xiaoben.ui.theme.CORNER_PERCENT

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
            Button(
                modifier = Modifier.height(210.dp).width(280.dp),
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { navController.navigate("chat") },
                content = {
                    Image(
                        painter = painterResource(R.drawable.traffic),
                        contentDescription = "交通事故"
                    )
                },
            )
            Button(
                modifier = Modifier.height(210.dp).width(280.dp),
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { navController.navigate("chat") },
                content = {
                    Image(
                        painter = painterResource(R.drawable.loan),
                        contentDescription = "民间借贷"
                    )
                }
            )
            Button(
                modifier = Modifier.height(210.dp).width(280.dp),
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { navController.navigate("chat") },
                content = {
                    Image(
                        painter = painterResource(R.drawable.hr),
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
            Button(
                modifier = Modifier.height(210.dp).width(280.dp),
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { navController.navigate("chat") },
                content = {
                    Image(
                        painter = painterResource(R.drawable.insurance),
                        contentDescription = "社会保险"
                    )
                }
            )
            Button(
                modifier = Modifier.height(210.dp).width(280.dp),
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { navController.navigate("chat") },
                content = {
                    Image(
                        painter = painterResource(R.drawable.litigation),
                        contentDescription = "诉讼指导"
                    )
                }
            )
            Button(
                modifier = Modifier.height(210.dp).width(280.dp),
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { navController.navigate("chat") },
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
                modifier = Modifier.height(70.dp).width(640.dp),
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
                content = { Text("咨询次数") }
            )
        }
    }
}

@Preview
@Composable
fun ComposablePreview() {
    HomePage(rememberNavController())
}
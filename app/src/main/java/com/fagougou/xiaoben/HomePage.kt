package com.fagougou.xiaoben

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.ui.theme.CORNER_PERCENT

@Composable
fun HomePage(navController:NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "法狗狗智能法律服务系统", fontSize = 42.sp)
        Text(text = "智能咨询", fontSize = 36.sp, modifier = Modifier.padding(horizontal = 36.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    navController.navigate("friendslist")
                },
                content = {
                    Image(
                        painter = painterResource(R.drawable.traffic),
                        contentDescription = "交通事故"
                    )
                },
            )
            Button(
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { },
                content = {
                    Image(
                        painter = painterResource(R.drawable.loan),
                        contentDescription = "民间借贷"
                    )
                }
            )
            Button(
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { },
                content = {
                    Image(
                        painter = painterResource(R.drawable.hr),
                        contentDescription = "企业人事"
                    )
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { },
                content = {
                    Image(
                        painter = painterResource(R.drawable.insurance),
                        contentDescription = "社会保险"
                    )
                }
            )
            Button(
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { },
                content = {
                    Image(
                        painter = painterResource(R.drawable.litigation),
                        contentDescription = "诉讼指导"
                    )
                }
            )
            Button(
                shape = RoundedCornerShape(CORNER_PERCENT),
                contentPadding = PaddingValues(0.dp),
                onClick = { },
                content = {
                    Image(
                        painter = painterResource(R.drawable.comsume),
                        contentDescription = "消费维权"
                    )
                }
            )
        }
        Text(text = "智能法律计算器", fontSize = 36.sp, modifier = Modifier.padding(horizontal = 36.dp))
    }
}
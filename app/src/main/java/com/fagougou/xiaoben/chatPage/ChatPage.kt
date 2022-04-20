package com.fagougou.xiaoben.chatPage

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.chatPage.ChatPage.history
import com.fagougou.xiaoben.utils.IFly

object ChatPage {
    val history = mutableStateListOf<String>()
}

@Composable
fun ChatPage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ){
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    content = { Text("返回",fontSize = 32.sp) }
                )
                Text("智能咨询",fontSize = 32.sp)
                Surface { }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for(i in 0..5)Button(
                    onClick = {  },
                    content = { Text("某一项",fontSize = 32.sp) }
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Top
        ) {
            for (text in history) Text(text, fontSize = 32.sp)
        }
        Column(
            modifier = Modifier.fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(IFly.volumeState.value, fontSize = 32.sp)
            Text(IFly.recognizeResult.value, fontSize = 32.sp)
        }
    }
}
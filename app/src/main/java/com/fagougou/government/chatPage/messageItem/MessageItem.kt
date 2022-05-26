package com.fagougou.government.chatPage.messageItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.VerticalGrid
import com.fagougou.government.model.CityMap
import com.fagougou.government.model.Message
import com.fagougou.government.model.Speaker
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MessageItem(message: Message, index: Int, scope: CoroutineScope, navController: NavController) {
    Row {
        if (!(ChatViewModel.history.getOrNull(index)?.speaker == Speaker.USER)) {
            if (index <= 0 || ChatViewModel.history[index-1].speaker == Speaker.USER) {
                Image(
                    painterResource(R.drawable.ic_robot_avatar), null,
                    Modifier.padding(start = 8.dp,top = 16.dp))
            } else Box(
                Modifier
                    .height(48.dp)
                    .width(56.dp))
        }
        when (message.speaker) {
            Speaker.ROBOT -> Row(
                Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) { MessageRect(message, index, scope) }
            Speaker.USER -> Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) { MessageRect(message, index, scope, Dodgerblue, Color.White) }
            Speaker.RECOMMEND -> Column(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 12.dp),
            ) {
                Surface(
                    Modifier.padding(start = 16.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(CORNER_FLOAT),
                    color = Color.White,
                ) {
                    Column( Modifier.padding(vertical = 16.dp,horizontal = 24.dp) ) {
                        if (message.isExpend) {
                            Row(
                                modifier = Modifier.padding(start = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (index == 1) Text("大家都在问:", fontSize = 24.sp)
                                else {
                                    Image(painterResource(R.drawable.ic_relate_question), null)
                                    Text(
                                        modifier = Modifier.padding(start = 12.dp),
                                        text = "相关问题",
                                        fontSize = 24.sp
                                    )
                                }
                            }
                            for (question in message.recommends) Button(
                                onClick = { scope.launch(Dispatchers.IO) {
                                    ChatViewModel.nextChat(
                                        question
                                    )
                                } },
                                content = {
                                    Text(
                                        "·$question",
                                        fontSize = 24.sp,
                                        color = Dodgerblue
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                elevation = ButtonDefaults.elevation(0.dp)
                            )
                        } else Row(
                            modifier = Modifier
                                .clickable {
                                    ChatViewModel.history[index] = message.copy(isExpend = true)
                                    if (index == ChatViewModel.history.lastIndex) scope.launch(
                                        Dispatchers.Main) {
                                        ChatViewModel.listState.scrollToItem(index + 2)
                                    }
                                }
                        ) {
                            Image(painterResource(R.drawable.ic_relate_question), null)
                            Text(
                                modifier = Modifier.padding(start = 12.dp),
                                text = "点击查看与您情况相关的问题",
                                fontSize = 24.sp,
                                color = Dodgerblue
                            )
                        }
                    }
                }
            }
            Speaker.OPTIONS -> Column(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 16.dp)
            ) {
                when (message.option.type) {
                    "radio" -> {
                        val lastIndex = message.option.items.lastIndex
                        for (y in 0..lastIndex step 4) Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (x in 0..3) {
                                val i = y + x
                                if (i <= lastIndex) {
                                    val item = message.option.items[i]
                                    Button(
                                        modifier = Modifier
                                            .height(60.dp)
                                            .width(180.dp),
                                        onClick = { scope.launch(Dispatchers.IO) {
                                            ChatViewModel.nextChat(
                                                item
                                            )
                                        } },
                                        content = { BasicText(item, 0.dp, 20.sp) },
                                        colors = ButtonDefaults.buttonColors(Dodgerblue)
                                    )
                                }
                            }
                        }
                    }
                    "address-with-search" -> {
                        if (ChatViewModel.currentProvince.value == "") {
                            val provinceList = CityMap.provinceList.keys.toList()
                            VerticalGrid(
                                provinceList,
                                6,
                                50,
                                150,
                                {  ChatViewModel.currentProvince.value = it }
                            )
                        } else {
                            val cityList = CityMap.provinceList[ChatViewModel.currentProvince.value] ?: listOf()
                            VerticalGrid(
                                cityList,
                                6,
                                50,
                                150,
                                {
                                    scope.launch(Dispatchers.IO) {
                                        ChatViewModel.nextChat("${ChatViewModel.currentProvince.value}-$it")
                                        ChatViewModel.currentProvince.value = ""
                                    }
                                }
                            )
                        }
                    }
                }
            }
            Speaker.COMPLEX -> Row(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) { ComplexRect(message, index, navController = navController) }
        }
    }
}
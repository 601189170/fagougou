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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.component.VerticalGrid
import com.fagougou.government.model.CityMap
import com.fagougou.government.model.Message
import com.fagougou.government.model.Speaker
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageItem(message: Message, index: Int, scope: CoroutineScope, navController: NavController,keyboardController: SoftwareKeyboardController?) {
    Row {
        when (message.speaker){
            Speaker.OPTIONS,Speaker.USER -> {}
            else -> {
                if (index <= 0 || ChatViewModel.history[index - 1].speaker == Speaker.USER) {
                    Image(
                        painterResource(R.drawable.ic_robot_avatar),
                        null,
                        Modifier.padding(start = 24.dp, top = 16.dp)
                    )
                } else Box(Modifier.padding(start = 16.dp).height(48.dp).width(56.dp))
            }
        }
        when (message.speaker) {
            Speaker.ROBOT -> Row(
                Modifier.fillMaxWidth(0.6f).padding(vertical = 12.dp),
                Arrangement.Start,
                Alignment.CenterVertically
            ) { MessageRect(message, index, scope, keyboardController) }
            Speaker.USER -> Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
                    .padding(vertical = 12.dp),
                Arrangement.End,
                Alignment.CenterVertically,
            ) { MessageRect(message, index, scope, keyboardController, Dodgerblue, Color.White) }
            Speaker.RECOMMEND -> Column( Modifier.padding(vertical = 12.dp)) {
                Surface(
                    Modifier.padding(start = 16.dp),
                    shape = RoundedCornerShape(CORNER_FLOAT),
                    color = Color.White,
                ) {
                    Column( Modifier.padding(vertical = 16.dp,horizontal = 24.dp) ) {
                        if (message.isExpend) {
                            Row(
                                Modifier.padding(start = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (index == 1) Text("大家都在问:", fontSize = 24.sp)
                                else {
                                    Image(painterResource(R.drawable.ic_relate_question), null)
                                    Text("相关问题",Modifier.padding(start = 12.dp),fontSize = 24.sp)
                                }
                            }
                            for (question in message.recommends) Button(
                                onClick = {
                                    keyboardController?.hide()
                                    scope.launch(Dispatchers.IO) { ChatViewModel.nextChat(question) }
                                },
                                content = {
                                    Text("·$question", fontSize = 24.sp, color = Dodgerblue)
                                },
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                elevation = ButtonDefaults.elevation(0.dp)
                            )
                        } else Row(
                            Modifier.clickable {
                                keyboardController?.hide()
                                ChatViewModel.history[index] = message.copy(isExpend = true)
                                if (index == ChatViewModel.history.lastIndex) scope.launch(
                                    Dispatchers.Main) {
                                    ChatViewModel.listState.scrollToItem(index + 2)
                                }
                            }
                        ) {
                            Image(painterResource(R.drawable.ic_relate_question), null)
                            Text("点击查看与您情况相关的问题",Modifier.padding(start = 12.dp),Dodgerblue,24.sp,)
                        }
                    }
                }
            }
            Speaker.OPTIONS -> Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                when (message.option.type) {
                    "radio" -> {
                        VerticalGrid(
                            message.option.items,
                            4,
                            64,
                            256,
                            { scope.launch(Dispatchers.IO) { ChatViewModel.nextChat(it) } },
                            windowWidth = 1114
                        )
                    }
                    "address-with-search" -> {
                        if (ChatViewModel.currentProvince.value == "") {
                            val provinceList = CityMap.provinceList.keys.toList()
                            VerticalGrid(provinceList, 6, 48, 192,
                                {  ChatViewModel.currentProvince.value = it }
                            )
                        } else {
                            val cityList = CityMap.provinceList[ChatViewModel.currentProvince.value] ?: listOf()
                            VerticalGrid(cityList, 6, 48, 192,
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
                Modifier.fillMaxWidth(0.6f).padding(vertical = 16.dp),
                Arrangement.Start,
                Alignment.CenterVertically
            ) { ComplexRect(message, index, keyboardController ,navController = navController) }
        }
    }
}
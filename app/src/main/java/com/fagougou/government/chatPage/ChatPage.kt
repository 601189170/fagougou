package com.fagougou.government.chatPage

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
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
import com.fagougou.government.chatPage.ChatViewModel.history
import com.fagougou.government.chatPage.ChatViewModel.listState
import com.fagougou.government.chatPage.ChatViewModel.selectedChatBot
import com.fagougou.government.chatPage.ChatViewModel.showBotMenu
import com.fagougou.government.chatPage.ChatViewModel.voiceInputMode
import com.fagougou.government.chatPage.messageItem.MessageItem
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel.constWechatUrl
import com.fagougou.government.consult.TouristsLoginActivity
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.model.Message
import com.fagougou.government.model.Speaker
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.IFly.wakeMode
import com.fagougou.government.utils.ImSdkUtils
import com.fagougou.government.utils.SafeBack.safeBack
import com.fagougou.government.utils.Tips

@Composable
fun ChatPage(navController: NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        wakeMode()
    }
    Column(
        Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            "智能咨询(${selectedChatBot.value})",
            navController,

            {
                with(DialogViewModel){
                    clear()
                    title = "温馨提示"
                    canExit = true
                    firstButtonText.value = "已经解决"
                    firstButtonOnClick.value = {
                        content.clear()
                        IFly.stopAll()
                        navController.safeBack()
                    }
                    secondButtonText.value = "转人工咨询"
                    secondButtonOnClick.value = {
                        content.clear()
                        IFly.stopAll()
                        val intent = Intent(activity, TouristsLoginActivity::class.java)
                        activity.startActivity(intent)
                    }
                    content.add(ContentStyle("退出前，请确认本次咨询是否解决您的问题？"))
                }
            },
            false,
            constWechatUrl
        )
        var lazyHeight = 952 - if(showBotMenu.value) 436 else 0
        lazyHeight -= if(voiceInputMode.value) 220 else 80
        LazyColumn(
            Modifier.height(lazyHeight.dp).fillMaxWidth().padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Top,
            state = listState,
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = Color(0x33FFFFFF),
                        shape = RoundedCornerShape(CORNER_FLOAT)
                    ) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(painterResource(R.drawable.ic_note),null)
                            Spacer( Modifier.width(16.dp).height(12.dp))
                            BasicText("温馨提示：咨询过程中可拿起电话咨询，声音效果更清晰！",0.dp,20.sp)
                        }
                    }
                }
            }
            items(history.size) { index -> MessageItem(history[index], index, scope, navController) }
            if (history.lastOrNull()?.speaker==Speaker.USER) item {
                MessageItem(Message(Speaker.ROBOT, content = ". . ."), -1, scope, navController)
            }
            item { Row( Modifier.fillMaxWidth().height(60.dp) ) {} }
        }
        InputBox(scope)
        if(showBotMenu.value) BotMenu()
    }
    BackHandler {}
}
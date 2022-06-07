package com.fagougou.government.chatPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputBox(scope: CoroutineScope){
    if(ChatViewModel.voiceInputMode.value) Surface(
        modifier = Modifier
            .height(260.dp)
            .width(880.dp)
            .padding(16.dp),
        color = Color(0x33FFFFFF),
        shape = RoundedCornerShape(CORNER_FLOAT)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                Modifier.padding(8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Image(
                    modifier = Modifier.clickable {
                        if(IFly.recognizeResult.value != IFly.UNWAKE_TEXT)return@clickable
                        ChatViewModel.voiceInputMode.value = false
                    },
                    painter = painterResource(id = R.drawable.ic_keyboard),
                    contentDescription = null)
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = IFly.recognizeResult.value,
                    fontSize = 21.sp,
                    color = Color.White
                )
                Image(
                    modifier = Modifier.clickable {
                        ChatViewModel.showBotMenu.value = true
                        ChatViewModel.voiceInputMode.value = false
                    },
                    painter = painterResource(id = R.drawable.ic_squad),
                    contentDescription = null)
            }
            PAG()
            Spacer(Modifier.height(12.dp).width(12.dp))
        }
    } else Column(
        Modifier.height(80.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val (text,bot) = remember{ FocusRequester.createRefs() }
        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextField(
                ChatViewModel.textInputContent.value,
                {
                    ChatViewModel.textInputContent.value = it
                    Router.lastTouchTime = Time.stampL
                },
                Modifier
                    .fillMaxWidth(0.85f)
                    .height(64.dp)
                    .focusRequester(text)
                    .onFocusChanged { state ->
                        if (state.isFocused) ChatViewModel.showBotMenu.value = false
                    }
                    .focusable(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color(0x33FFFFFF),
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent
                ),
                placeholder = {Text("请输入问题..",color = Color.Gray, fontSize = 20.sp)},
                textStyle = TextStyle(fontSize = 20.sp),
                shape = RoundedCornerShape(45),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        val content = ChatViewModel.textInputContent.value
                        ChatViewModel.textInputContent.value = ""
                        scope.launch(Dispatchers.IO) { ChatViewModel.nextChat(content) }
                    }
                ),

            )
            Button(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .width(64.dp)
                    .height(64.dp),
                content = { Image(painterResource(R.drawable.ic_microphone),null) },
                onClick = {
                    ChatViewModel.showBotMenu.value = false
                    ChatViewModel.voiceInputMode.value = true
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(8.dp)
            )
            Button(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .width(64.dp)
                    .height(64.dp)
                    .focusRequester(bot)
                    .focusable(),
                content = {
                    Image(
                        painterResource(
                            if (!ChatViewModel.showBotMenu.value) R.drawable.ic_squad
                            else R.drawable.ic_close_ly
                        ),
                        null
                    )
                },
                onClick = {
                    ChatViewModel.showBotMenu.value = !ChatViewModel.showBotMenu.value
                    ChatViewModel.voiceInputMode.value = false
                    text.freeFocus()
                    bot.requestFocus()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(8.dp)
            )
        }
    }
}
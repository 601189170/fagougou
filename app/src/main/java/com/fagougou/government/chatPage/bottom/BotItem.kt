package com.fagougou.government.chatPage.bottom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.component.BasicText
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BotItem(modifier: Modifier, data:Pair<String,Int>){
    val scope = rememberCoroutineScope()
    Column(
        modifier,
        Arrangement.Top,
        Alignment.CenterHorizontally
    ){
        Button(
            {
                with(DialogViewModel) {
                    clear()
                    title = "切换领域"
                    firstButtonText.value = "取消"
                    firstButtonOnClick.value = { content.clear() }
                    secondButtonText.value = "确定"
                    secondButtonOnClick.value = {
                        content.clear()
                        ChatViewModel.showBotMenu.value = false
                        ChatViewModel.selectedChatBot.value = data.first
                        scope.launch(Dispatchers.IO) { ChatViewModel.startChat() }
                    }
                    content.add(ContentStyle("选择更换领域后，当前的咨询记录会清除，是否确定更换？"))
                }
            },
            Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            content = {
                Surface(color = Color.Transparent) {
                    Image(painterResource(data.second),null)
                    if( ChatViewModel.selectedChatBot.value == data.first ) Column(
                        Modifier.fillMaxWidth(),
                        Arrangement.Top,
                        Alignment.End
                    ) {
                        Image(painterResource(R.drawable.ic_select_note), "Selected")
                    }
                }
            },
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.elevation(0.dp)
        )
        BasicText( data.first, 12.dp, 18.sp )
    }
}
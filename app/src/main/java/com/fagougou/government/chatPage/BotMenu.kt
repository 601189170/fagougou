package com.fagougou.government.chatPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.fagougou.government.R
import com.fagougou.government.component.VerticalGrid
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BotMenu() {
    val scope = rememberCoroutineScope()
    val botResMap = mapOf(
        Pair("公司财税", R.drawable.bot_square_tax),
        Pair("交通事故", R.drawable.bot_square_traffic),
        Pair("婚姻家事", R.drawable.bot_square_marry),
        Pair("员工纠纷", R.drawable.bot_square_employee),
        Pair("知识产权", R.drawable.bot_square_knowledge),
        Pair("刑事犯罪", R.drawable.bot_square_crime),
        Pair("企业人事", R.drawable.bot_square_employer),
        Pair("房产纠纷", R.drawable.bot_square_house),
        Pair("民间借贷", R.drawable.bot_square_loan),
        Pair("消费维权", R.drawable.bot_square_consumer),
    )
    VerticalGrid(
        botResMap.toList(),
        8,
        128,
        128,
        onClick = {
            with(DialogViewModel) {
                clear()
                title = "温馨提示"
                firstButtonText.value = "取消"
                firstButtonOnClick.value = { content.clear() }
                secondButtonText.value = "确定"
                secondButtonOnClick.value = {
                    content.clear()
                    ChatViewModel.selectedChatBot.value = it.first
                    scope.launch(Dispatchers.IO) { ChatViewModel.startChat() }
                }
                content.add( ContentStyle( "更换领域后，当前的记录会清除" ) )
            }
        },
        selected = { ChatViewModel.selectedChatBot.value == it.first },
        backgound = Color.Transparent
    )
}
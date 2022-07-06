package com.fagougou.government.chatPage

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fagougou.government.R
import com.fagougou.government.chatPage.bottom.BotItem
import com.fagougou.government.component.VerticalGrid

@Composable
fun BotMenu() {
    val botResMap = mapOf(
        Pair("公司财税", R.drawable.bot_square_tax),
        Pair("交通事故", R.drawable.bot_square_traffic),
        Pair("婚姻家事", R.drawable.bot_square_marry),
        Pair("员工纠纷", R.drawable.bot_square_employee),
        Pair("知识产权", R.drawable.bot_square_knowledge),
        Pair("刑事犯罪", R.drawable.bot_square_crime),
        Pair("房产纠纷", R.drawable.bot_square_house),
        Pair("企业人事", R.drawable.bot_square_employer),
        Pair("消费维权", R.drawable.bot_square_consumer),
        Pair("民间借贷", R.drawable.bot_square_loan),
    )
    Spacer(Modifier.height(40.dp))
    VerticalGrid(botResMap.toList(),6,36.dp,164.dp,128.dp){ modifier, pair ->
        BotItem(modifier, pair)
    }
}
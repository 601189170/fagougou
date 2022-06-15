package com.fagougou.government.chatPage.messageItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R
import com.fagougou.government.ui.theme.CORNER_FLOAT8

@Composable
fun AboutCase (){

    val categoryList = arrayListOf<String>("郑瑞英与黄绍飞离婚纠纷一审民事判决","郑瑞英与黄绍飞离婚纠纷一审民事判决","郑瑞英与黄绍飞离婚纠纷一审民事判决")
    LazyColumn(
        Modifier.padding(top = 16.dp,start = 60.dp,end = 60.dp),
        horizontalAlignment= Alignment.CenterHorizontally
    ) {
        items(categoryList){
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable { }
                .padding(top = 24.dp)
                .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8))
                ,contentAlignment = Alignment.CenterStart) {
                Row(verticalAlignment= Alignment.CenterVertically ,
                    modifier = Modifier.padding(top = 32.dp, start = 32.dp, bottom = 32.dp)) {
                    Image(painterResource(R.drawable.ic_icon_aboutcase), null)
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = it,
                            fontSize = 24.sp,
                            color = Color(0xFF222222)
                        )
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "日期："+"2015-07-07"+"  "+"广东省深圳市南山区人民法院",
                            fontSize = 20.sp,
                            color = Color(0xFF909499)
                        )
                    }
                }
            }
        }

    }

}
package com.fagougou.government.chatPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.chatPage.ReportMainModel.laws
import com.fagougou.government.ui.theme.CORNER_FLOAT8

@Composable
fun LawsPage (navController: NavController){
    LazyColumn(
        Modifier.padding(top = 16.dp,start = 60.dp,end = 60.dp),
        horizontalAlignment=Alignment.CenterHorizontally
    ) {
        items(laws.size){

            Box( modifier = Modifier
                .padding(top = 24.dp)
                .clickable {
                    if (laws[it].isExpan) laws[it]= laws[it].copy(isExpan = false) else laws[it]= laws[it].copy(isExpan = true)
                }
                .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8))
                ,contentAlignment = Alignment.Center){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
                ) {
                    Row(
                        horizontalArrangement=Arrangement.SpaceBetween,
                        verticalAlignment=Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp, start = 32.dp, bottom = 32.dp, end = 32.dp)) {
                        Column {
                            Text(
                                modifier = Modifier.padding(end = 50.dp).width(550.dp),
                                fontWeight = FontWeight.Bold,
                                text = laws[it].law,
                                letterSpacing = 1.2f.sp,
                                fontSize = 24.sp,
                                color = Color(0xFF222222)
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = laws[it].tiaoMu,
                                fontSize = 20.sp,
                                color = Color(0xFF0F87FF)
                            )
                        }
                        Image(painterResource(if (!laws[it].isExpan) R.drawable.ic_icon_down else R.drawable.ic_icon_up), null)
                    }
                    if (laws[it].isExpan){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ){
                            Text(

                                text = laws[it].content,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 32.dp, bottom = 32.dp, end = 24.dp),
                                color = Color(0xFF666666),
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


package com.fagougou.government.chatPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScopeMarker
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Surface
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
import com.fagougou.government.homePage.HomeButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Report (){
    val botResMap = mapOf(
        Pair("对家庭不负责任", R.drawable.bot_tax),

    )
    val botList = botResMap.toList()
   Column(
       Modifier
           .fillMaxSize()
           .padding(horizontal = 100.dp)) {
       Row(Modifier.padding(top = 40.dp)) {
        //修改
           LazyVerticalGrid(userScrollEnabled = false,columns = GridCells.Fixed(5),
           content = {
               items(botResMap.toList()){ bot ->
                   Column(
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally){
                       Text(
                           modifier = Modifier
                               .padding(horizontal = 20.dp, vertical = 8.dp)
                               .background(Color(0xFFECF5FF)),
                           text = "对家庭不负责任",
                           fontSize = 20.sp,
                           color = Color(0xFF0E7AE6)
                       )
                   }
               }
           })
           
       }
       Surface(
           Modifier
               .fillMaxWidth()
               .height(1.dp),color = Color.Gray) {}
       Image(painterResource( R.drawable.ic_icon_rp1 ), null)



   }

}
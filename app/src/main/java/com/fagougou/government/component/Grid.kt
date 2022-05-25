package com.fagougou.government.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.R
import com.fagougou.government.ui.theme.Dodgerblue

@Composable
fun <T> VerticalGrid(datas:List<T>, columnNumber:Int, height: Int, width:Int, onClick:(T) -> Unit, selected:(T) -> Boolean = {false}, backgound:Color = Dodgerblue){
    val lastIndex = datas.lastIndex
    val padding = (1280-(columnNumber*width))/(columnNumber+1)
    for (y in 0..lastIndex step columnNumber) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top =  padding.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            for (x in 0 until columnNumber) {
                val i = y + x
                if (y + x <= lastIndex) GridItem(datas[i], height.dp , (width+padding).dp, padding.dp, onClick, selected, backgound)
            }
        }
    }
}

@Composable
fun <T> GridItem(data:T, height: Dp, width:Dp, padding:Dp, onClick: (T) -> Unit, selected:(T) -> Boolean, backgound:Color = Dodgerblue){
    Column(
        modifier = Modifier.width(width).padding(start = padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            modifier = Modifier.height(height).fillMaxWidth(),
            onClick = { onClick.invoke(data )},
            colors = ButtonDefaults.buttonColors(backgound),
            content = {
                Surface(color = Color.Transparent) {
                    when(data) {
                        is String -> BasicText(data, 0.dp, 21.sp)
                        is Pair<*,*> -> Image(painterResource(data.second as Int),null)
                    }
                    if(selected.invoke(data)) Column(
                        modifier = Modifier.width(width),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Image(painterResource(R.drawable.ic_select_note), "Selected")
                    }
                }
            },
            contentPadding = PaddingValues(
                when(data) {
                    is String -> 4.dp
                    else -> 0.dp
                }
            ),
            elevation = ButtonDefaults.elevation(0.dp)
        )
        if(data is Pair<*,*>)BasicText( data.first as String, 12.dp, 18.sp )
    }
}
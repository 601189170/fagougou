package com.fagougou.government.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.ui.theme.Dodgerblue

@Composable
fun <T> VerticalGrid(
    dataList:List<T>,
    columnNumber:Int,
    verticalSpacer:Dp,
    contentHeight:Dp,
    contentWidth:Dp,
    content:@Composable (Modifier,T)->Unit
){
    Column {
        val itemModifier = Modifier.height(contentHeight).width(contentWidth)
        for (y in 0..dataList.lastIndex step columnNumber) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = verticalSpacer),
                Arrangement.SpaceEvenly,
                Alignment.CenterVertically
            ) {
                for (x in 0 until columnNumber) {
                    val i = y + x
                    if (i <= dataList.lastIndex) content(itemModifier,dataList[i])
                    else Spacer(itemModifier)
                }
            }
        }
    }
}

@Composable
fun GridButtonItem(modifier:Modifier, data:String, background:Color = Dodgerblue, onClick: (String) -> Unit){
    Button(
        { onClick.invoke(data)},
        modifier,
        colors = ButtonDefaults.buttonColors(background),
        content = {
            Surface(color = Color.Transparent) {
                BasicText(data, 0.dp, 21.sp)
            }
        },
        contentPadding = PaddingValues(4.dp),
        elevation = ButtonDefaults.elevation(0.dp)
    )
}
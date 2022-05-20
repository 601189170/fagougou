package com.fagougou.government.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.dialog.DialogViewModel.content
import com.fagougou.government.dialog.DialogViewModel.firstButtonOnClick
import com.fagougou.government.dialog.DialogViewModel.firstButtonText
import com.fagougou.government.dialog.DialogViewModel.secondButtonOnClick
import com.fagougou.government.dialog.DialogViewModel.secondButtonText
import com.fagougou.government.dialog.DialogViewModel.title
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue

object DialogViewModel {
    var title = ""
    val content = mutableStateOf("")
    var firstButtonText = mutableStateOf("")
    var secondButtonText = mutableStateOf("")
    var firstButtonOnClick = mutableStateOf({ })
    var secondButtonOnClick = mutableStateOf({ })

    fun clear(){
        title = ""
        content.value = ""
        firstButtonText.value = ""
        secondButtonText.value = ""
        firstButtonOnClick.value = {}
        secondButtonOnClick.value = {}
    }
}

@Composable
fun Dialog(){
    if (content.value.isNotBlank()) Surface(color= Color(0x3300000)) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(CORNER_FLOAT),
                elevation = 2.dp
            ) {
                Column(
                    Modifier
                        .width(640.dp)
                        .height(320.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(title,fontSize = 28.sp)
                    Text(content.value,fontSize = 24.sp,color = Color.DarkGray)
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if(!firstButtonText.value.isNullOrBlank())Button(
                            onClick = firstButtonOnClick.value,
                            content = {
                                Text(
                                    modifier = Modifier.padding(12.dp),
                                    text = firstButtonText.value,
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
                        )
                        if(!secondButtonText.value.isNullOrBlank())Button(
                            onClick = secondButtonOnClick.value,
                            content = {
                                Text(
                                    modifier = Modifier.padding(12.dp),
                                    text = secondButtonText.value,
                                    fontSize = 24.sp,
                                    color = Dodgerblue
                                )
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(2.dp, Dodgerblue)
                        )
                    }
                }
            }
        }
    }
}
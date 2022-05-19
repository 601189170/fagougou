package com.fagougou.government.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
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
import com.fagougou.government.dialog.DialogViewModel.showChangeRobotDialog
import com.fagougou.government.dialog.DialogViewModel.showRouteRemainDialog
import com.fagougou.government.dialog.DialogViewModel.title
import com.fagougou.government.ui.theme.CORNER_FLOAT

object DialogViewModel {
    var title = ""
    val content = mutableStateOf("")
    var firstButtonText = ""
    var secondButtonText = ""
    var firstButtonOnClick = {}
    var secondButtonOnClick = {}
    val showRouteRemainDialog = mutableStateOf(false)
    val showChangeRobotDialog = mutableStateOf(false)

    fun clear(){
        showRouteRemainDialog.value = false
        showChangeRobotDialog.value = false
        title = ""
        content.value = ""
        firstButtonText = ""
        secondButtonText = ""
        firstButtonOnClick = {}
        secondButtonOnClick = {}
    }
}

@Composable
fun Dialog(){
    val needDialog = showRouteRemainDialog.value || showChangeRobotDialog.value
    if (needDialog) Surface(color= Color(0x3300000)) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(CORNER_FLOAT)
            ) {
                Column(
                    Modifier.width(600.dp).height(300.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(title,fontSize = 24.sp)
                    Text(content.value,fontSize = 20.sp,color = Color.DarkGray)
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if(!firstButtonText.isNullOrBlank())Button(
                            onClick = firstButtonOnClick,
                            content = { Text(firstButtonText)}
                        )
                        if(!secondButtonText.isNullOrBlank())Button(
                            onClick = secondButtonOnClick,
                            content = { Text(secondButtonText) }
                        )
                    }
                }
            }
        }
    }
}
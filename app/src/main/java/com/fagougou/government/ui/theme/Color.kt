package com.fagougou.government.ui.theme

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Dodgerblue = Color(0xFF1e90ff)

@Composable
fun WhiteTextFieldColor():TextFieldColors{
    return TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
}

@Composable
fun Alpha33WhiteTextFieldColor():TextFieldColors{
    return TextFieldDefaults.textFieldColors(
        backgroundColor = Color(0x33FFFFFF),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )
}

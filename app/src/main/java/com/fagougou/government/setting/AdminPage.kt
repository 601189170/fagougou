package com.fagougou.government.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.setting.AdminPage.text
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue

object AdminPage{
    val text = mutableStateOf("")
}

@Composable
fun AdminPage(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header("管理员密码",navController,{ text.value = "" })
        val textFieldColors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0x44FFFFFF),
            cursorColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
        TextField(
            text.value,
            {text.value = it},
            Modifier.padding(top = 320.dp).width(480.dp),
            textStyle = TextStyle(color = Color.White, fontSize = 28.sp),
            placeholder = {Text("请输入管理员密码",color = Color.Gray, fontSize = 28.sp)},
            shape = RoundedCornerShape(CORNER_FLOAT),
            colors = textFieldColors,
            leadingIcon = { Image(painterResource(R.drawable.ic_key), null, modifier = Modifier.padding(horizontal = 24.dp)) }
        )
        Button(
            onClick = {
                if (text.value == "faxiaomeng") {
                    text.value = ""
                    navController.popBackStack()
                    navController.navigate(Router.settings)
                }
            },
            Modifier.padding(top = 24.dp).width(480.dp).height(60.dp),
            content = { BasicText("确认") },
            colors = ButtonDefaults.buttonColors(Dodgerblue)
        )
    }
    BackHandler{}
}
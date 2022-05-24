package com.fagougou.government.dialog

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.dialog.DialogViewModel.clear
import com.fagougou.government.R
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.dialog.DialogViewModel.content
import com.fagougou.government.dialog.DialogViewModel.firstButtonOnClick
import com.fagougou.government.dialog.DialogViewModel.firstButtonText
import com.fagougou.government.dialog.DialogViewModel.icon
import com.fagougou.government.dialog.DialogViewModel.type
import com.fagougou.government.dialog.DialogViewModel.secondButtonOnClick
import com.fagougou.government.dialog.DialogViewModel.secondButtonText
import com.fagougou.government.dialog.DialogViewModel.title
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object DialogViewModel {
    var icon = 0
    var title = ""
    val content = mutableStateListOf<ContentStyle>()
    var type = "button"
    var firstButtonText = mutableStateOf("")
    var secondButtonText = mutableStateOf("")
    var firstButtonOnClick = mutableStateOf({})
    var secondButtonOnClick = mutableStateOf({})

    fun clear() {
        icon = 0
        title = ""
        content.clear()
        type = "button"
        firstButtonText.value = ""
        secondButtonText.value = ""
        firstButtonOnClick.value = {}
        secondButtonOnClick.value = {}
    }

    fun startPrint(scope: CoroutineScope) {
        clear()
        icon = R.drawable.ic_painter_blue
        title = "正在打印"
        content.add(ContentStyle("文件正在打印，请耐心等待..."))
        scope.launch(Dispatchers.Default) {
            delay(2500)
            if (content.firstOrNull()?.content?.contains("文件正在打印") == true) clear()
        }
    }
}

@Composable
fun Dialog() {
    if (content.firstOrNull()?.content?.isNotBlank() == true) Surface(
        Modifier.clickable { if(type == "nameDef")clear() },
        color = Color(0x33000000)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (type) {
                "button" -> ButtonDialog()
                "nameDef" -> NameDefDialog()
            }
        }
    }
}

@Composable
fun ButtonDialog() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(CORNER_FLOAT),
        elevation = 2.dp
    ) {
        Column(
            Modifier
                .width(640.dp)
                .height(288.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (icon != 0){
                Spacer(Modifier.height(16.dp).height(16.dp))
                Image(painterResource(icon), null)
            }
            Text(title, fontSize = 28.sp)
            val annotatedString = buildAnnotatedString {
                content.forEach{
                    if (!TextUtils.isEmpty(it.content)){
                        if (it.style==0) append(it.content)
                        else withStyle(style = SpanStyle(color = Color(0xEEDD3344))){ append(it.content) }
                    }
                }
            }
            ClickableText(
                modifier = Modifier.padding(12.dp),
                text = annotatedString, style = MaterialTheme.typography.h5, onClick = {}
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (!firstButtonText.value.isNullOrBlank()) Button(
                    onClick = firstButtonOnClick.value,
                    content = {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = firstButtonText.value,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                )
                if (!secondButtonText.value.isNullOrBlank()){
                    Spacer(Modifier.width(36.dp).height(36.dp))
                    Button(
                        onClick = secondButtonOnClick.value,
                        content = {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = secondButtonText.value,
                                fontSize = 24.sp,
                                color = Dodgerblue
                            )
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        border = BorderStroke(2.dp, Dodgerblue),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NameDefDialog() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(CORNER_FLOAT),
        elevation = 2.dp
    ) {
        Column(
            Modifier
                .width(720.dp)
                .height(288.dp)
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 28.sp)
            Text(content.firstOrNull()?.content?:"", fontSize = 24.sp, color = Color.DarkGray)
        }
    }
    Row( Modifier.padding(top = 32.dp) ) {
        Image(painterResource(R.drawable.ic_close), null)
    }
}
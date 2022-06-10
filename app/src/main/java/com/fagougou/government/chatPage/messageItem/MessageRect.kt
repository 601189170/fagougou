package com.fagougou.government.chatPage.messageItem

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.component.BasicText
import com.fagougou.government.model.Message
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LawExpend(message: Message, index: Int,keyboardController: SoftwareKeyboardController?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    keyboardController?.hide()
                    val isExpend = !message.isExpend
                    ChatViewModel.history[index] = message.copy(isExpend = isExpend)
                }
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "法律依据",
                fontSize = 20.sp,
                color = Dodgerblue
            )
            val svg = if (message.isExpend) R.drawable.ic_flod else R.drawable.ic_expend
            Image(painterResource(svg), null)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ){
            if (message.isExpend) for ((i, law) in message.laws.withIndex()) {
                Text(
                    (i + 1).toString() + "." + law.name + law.position + ":",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 24.sp
                )
                Text(
                    law.content,
                    modifier = Modifier.padding(12.dp),
                    color = Color(0xFF666666),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageRect(
    message: Message,
    index: Int,
    scope: CoroutineScope,
    keyboardController:SoftwareKeyboardController?,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
) {
    Surface(
        Modifier.padding(start = 16.dp),
        shape = RoundedCornerShape(CORNER_FLOAT),
        color = backgroundColor,
    ) {
        Column(Modifier.padding(vertical = 16.dp,horizontal = 24.dp) ) {
            if (message.listDef.isEmpty()){


                Text(
                    message.content + message.complex.explanation,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = textColor,
                )
            }else{
                val annotatedString = buildAnnotatedString {
                    message.listDef.forEach{
                        if (!TextUtils.isEmpty(it.content)){
                            if (it.style==0) append(it.content)
                            else{
                                pushStringAnnotation(tag = "policy", annotation = it.content)
                                withStyle(SpanStyle(Dodgerblue)) { append(" 「"+it.content+"」 ") }
                                pop()
                            }
                        }
                    }
                }
                ClickableText(
                    modifier = Modifier.padding(12.dp),
                    text = annotatedString,
                    style = TextStyle(
                        color = textColor,
                        fontSize = 24.sp,
                        lineHeight=38.sp,
                    ),
                    onClick = { offset ->
                        keyboardController?.hide()
                        annotatedString.getStringAnnotations("policy", offset, offset).firstOrNull()?.let {
                            ChatViewModel.getDefInfo(it.item);
                        }
                    }
                )
            }
            for(question in message.inlineRecommend){
                Text(
                    question,
                    Modifier.clickable {
                        keyboardController?.hide()
                        scope.launch(Dispatchers.IO) { ChatViewModel.nextChat(question) }
                    },
                    fontSize = 24.sp,
                    color = Dodgerblue,
                )
            }

            if (message.laws.isNotEmpty()){
                Divider(
                    Modifier.padding(top = 16.dp),
                    color = Color(0xFFCCCCCC),
                    thickness = 2.dp,
                )
            }
            if (message.laws.isNotEmpty()) LawExpend(message, index,keyboardController)
        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComplexRect(
    message: Message,
    index: Int,
    keyboardController:SoftwareKeyboardController?,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    navController: NavController

) {
    Surface(
        Modifier.padding(start = 16.dp),
        shape = RoundedCornerShape(CORNER_FLOAT),
        color = backgroundColor,
    ) {
        Column(
            modifier = Modifier.clickable {
                keyboardController?.hide()
                ChatViewModel.getComplex(message.complex.attachmentId, navController)
            }
        ) {
            Surface(color = Dodgerblue) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) { BasicText(message.complex.title) }
            }
            Text(
                modifier = Modifier.padding(vertical = 16.dp,horizontal = 24.dp),
                text = message.content + message.complex.explanation,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = textColor,
            )
            Divider(
                color = Color(0xFFCCCCCC),
                thickness = 2.dp
            )
            if (message.laws.isNotEmpty()) LawExpend(message, index,keyboardController)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    color= Color(0xFF0E7AE6),
                    text = "点击查看",
                    fontSize = 20.sp,
                )
                val svg = R.drawable.ic_icon_right_mark
                Image(painterResource(svg), null)
            }
        }
    }
}
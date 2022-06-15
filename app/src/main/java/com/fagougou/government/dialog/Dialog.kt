package com.fagougou.government.dialog

import android.text.TextUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fagougou.government.CommonApplication
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.component.BasicText
import com.fagougou.government.contractPage.ContractViewModel


import com.fagougou.government.dialog.DialogViewModel.canExit
import com.fagougou.government.dialog.DialogViewModel.clear
import com.fagougou.government.dialog.DialogViewModel.content
import com.fagougou.government.dialog.DialogViewModel.firstButtonOnClick
import com.fagougou.government.dialog.DialogViewModel.firstButtonText
import com.fagougou.government.dialog.DialogViewModel.icon
import com.fagougou.government.dialog.DialogViewModel.secondButtonOnClick
import com.fagougou.government.dialog.DialogViewModel.secondButtonText
import com.fagougou.government.dialog.DialogViewModel.title
import com.fagougou.government.dialog.DialogViewModel.type
import com.fagougou.government.generateContract.GenerateContract
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.Printer
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips
import com.fagougou.government.utils.Tips.toast
import timber.log.Timber
import java.io.File
import java.lang.Exception

object DialogViewModel {
    var icon = 0
    var title = ""
    val content = mutableStateListOf<ContentStyle>()
    var type = "button"
    var canExit = false
    val firstButtonText = mutableStateOf("")
    val secondButtonText = mutableStateOf("")
    val firstButtonOnClick = mutableStateOf({})
    val secondButtonOnClick = mutableStateOf({})

    fun clear() {
        icon = 0
        title = ""
        content.clear()
        type = "button"
        canExit = false
        firstButtonText.value = ""
        secondButtonText.value = ""
        firstButtonOnClick.value = {}
        secondButtonOnClick.value = {}
    }

    fun confirmPrint(mode:String) {
        GenerateContract.lastModifier = Time.stamp
        clear()
        title = "即将进行打印"
        content.add(ContentStyle("按下确认键开始打印当前合同"))
        canExit = true
        firstButtonText.value = "取消"
        secondButtonText.value = "确认"
        firstButtonOnClick.value = { clear() }
        secondButtonOnClick.value = {
            startPrint()
            when(mode){
                "pdf" -> {
                    try{
                        val pdfFile = File(activity.cacheDir, "${ContractViewModel.pdfFile?.id ?: "0"}.pdf")
                        Printer.printPdf(pdfFile)
                    }catch (e:Exception){
                        Timber.e(e)
                        toast(e.toString())
                        clear()
                    }

                }
                "webView" -> Printer.webViewPrint.value=true
                else -> clear()
            }
        }
    }

    fun startPrint() {
        clear()
        icon = R.drawable.ic_painter_blue
        title = "正在打印"
        content.add(ContentStyle("文件正在打印，预计需要30秒。请耐心等待..."))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Dialog() {
    if (content.firstOrNull()?.content?.isNotBlank() == true) {
        LocalSoftwareKeyboardController.current?.hide()
        Surface(
            Modifier.clickable { if(type == "nameDef")clear() },
            color = Color(0x33000000)
        ) {
            Column(
                Modifier.fillMaxSize(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                when (type) {
                    "button" -> ButtonDialog()
                    "nameDef" -> NameDefDialog()
                }
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
                .height(288.dp)
                .padding(vertical = 4.dp, horizontal = 40.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (icon != 0){
                Spacer(
                    Modifier
                        .height(10.dp)
                        .height(16.dp))
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
                modifier = Modifier.padding(10.dp),
                style = TextStyle(
                    textAlign= TextAlign.Center,
                    color = Color.Black,
                    fontSize = 24.sp,
                    lineHeight=38.sp,
                ),
                text = annotatedString, onClick = {}
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (!firstButtonText.value.isNullOrBlank()) Button(
                    onClick = firstButtonOnClick.value,
                    content = { BasicText(firstButtonText.value) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue),
                    contentPadding = PaddingValues(horizontal = 36.dp,vertical = 12.dp),
                    elevation = ButtonDefaults.elevation(0.dp,0.dp),
                    shape = RoundedCornerShape(12)
                )
                if (!secondButtonText.value.isNullOrBlank()){
                    Spacer(
                        Modifier
                            .width(36.dp)
                            .height(36.dp))
                    Button(
                        onClick = secondButtonOnClick.value,
                        content = { BasicText(secondButtonText.value, color = Dodgerblue) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        border = BorderStroke(2.dp, Dodgerblue),
                        contentPadding = PaddingValues(horizontal = 36.dp,vertical = 12.dp),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp),
                        shape = RoundedCornerShape(12)
                    )
                }
            }
        }
        if (canExit)Row(
            Modifier
                .width(640.dp)
                .height(288.dp)
                .padding(start = 40.dp, end = 40.dp, top = 24.dp),
            horizontalArrangement = Arrangement.End
        ){
            Image(
                painterResource(R.drawable.ic_close),
                "Close Dialog",
                modifier = Modifier
                    .height(32.dp)
                    .width(32.dp)
                    .clickable { clear() },
                colorFilter = ColorFilter.tint(Color.DarkGray))
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
            Text(content.firstOrNull()?.content?:"", fontSize = 24.sp, color = Color.DarkGray,lineHeight =35.sp )
        }
    }
    Row( Modifier.padding(top = 32.dp) ) {
        Image(painterResource(R.drawable.ic_close), null)
    }
}


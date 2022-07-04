package com.fagougou.government.generateContract

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.generateContract.GenerateContractViewModel.lastModifier
import com.fagougou.government.CommonApplication.Companion.presentation
import com.fagougou.government.generateContract.GenerateContractModel.readhtml
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.ui.theme.WhiteTextFieldColor
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*
object GenerateContractModel{
    val readhtml = mutableStateOf(false)
}
@Composable
fun GenerateContract(navController: NavController) {
    LaunchedEffect(null) {
        presentation?.playVideo(R.raw.vh_generate_contract)
        while (isActive) {
            delay(1000)
            GenerateContractViewModel.updateContent()
        }
    }
    Surface(color = Color.White) {
        Column(Modifier.fillMaxSize(), Arrangement.Top) {
            Header("智能文书", navController, { GenerateContractViewModel.clear() })
            Row(Modifier.fillMaxSize()) {
                val scrollState = rememberScrollState()
                Column(
                    Modifier.fillMaxHeight().fillMaxWidth(0.6f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(Modifier.fillMaxHeight(0.88f)) {
                        GenerateWebView()
                    }
                    Divider(thickness = 2.dp)
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                         Button({readhtml.value=true },
                            Modifier
                                .height(60.dp)
                                .width(200.dp),
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(painterResource(R.drawable.ic_wechat), null)
                                    BasicText("微信查看",0.dp,21.sp)
                                }
                            },
                            colors = buttonColors(backgroundColor = Dodgerblue),
                            shape = RoundedCornerShape(18)
                        )
                        Button(
                            { DialogViewModel.confirmPrint("webView") },
                            Modifier
                                .height(60.dp)
                                .width(200.dp),
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp),
                            content = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(painterResource(R.drawable.ic_painter), null)
                                    BasicText("打印合同",0.dp,21.sp)
                                }
                            },
                            colors = buttonColors(backgroundColor = Dodgerblue),
                            shape = RoundedCornerShape(16)
                        )
                    }
                }
                Divider(
                    Modifier
                        .fillMaxHeight()
                        .width(1.dp))
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                ) {
                    for (item in GenerateContractViewModel.formList) {
                        Text(
                            item.label,
                            Modifier.padding(top = 24.dp, start = 16.dp),
                            fontSize = 24.sp
                        )
                        for (child in item.children) {
                            Column(Modifier.padding(horizontal = 16.dp)) {
                                Row(
                                    Modifier.padding(top = 24.dp, bottom = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(Modifier.height(24.dp).width(4.dp), color = Dodgerblue) { }
                                    Text(child.label, Modifier.padding(start = 12.dp), fontSize = 18.sp)
                                }
                                when (child.type) {
                                    "checkbox" -> {
                                        Column {
                                            for ((i, option) in child.values.withIndex()) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Checkbox( i in child.selected,
                                                        {
                                                            if (it) child.selected.add(i)
                                                            else child.selected.remove(i)
                                                            lastModifier = child.variable
                                                        }
                                                    )
                                                    Text(option)
                                                }
                                            }
                                        }
                                    }
                                    "select" -> {
                                        Column {
                                            for ((i, option) in child.values.withIndex()) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    RadioButton( i in child.selected,
                                                        {
                                                            child.selected.clear()
                                                            child.selected.add(i)
                                                            lastModifier = child.variable
                                                        }
                                                    )
                                                    Text(option)
                                                }
                                            }
                                        }
                                    }
                                    else -> TextField(
                                        child.input.value,
                                        {
                                            Router.lastTouchTime = Time.stamp
                                            child.input.value = it
                                            lastModifier = child.variable
                                        },
                                        Modifier
                                            .fillMaxWidth()
                                            .height(if (child.label.contains("地址")) 112.dp else 56.dp)
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(18)),
                                        keyboardOptions = if (child.label.contains("电话")){
                                            KeyboardOptions(keyboardType = KeyboardType.Number,imeAction = ImeAction.Next)
                                        } else {
                                            KeyboardOptions(keyboardType = KeyboardType.Text,imeAction = ImeAction.Next)
                                        },
                                        colors = WhiteTextFieldColor(),
                                        placeholder = { if (child.input.value == "") Text(child.comment) }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.width(56.dp).height(112.dp))
                }
            }
        }
    }
}
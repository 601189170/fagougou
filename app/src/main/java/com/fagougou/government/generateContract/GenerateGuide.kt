package com.fagougou.government.generateContract

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.Header
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.generateContract.GenerateContractViewModel.contractList
import com.fagougou.government.generateContract.GenerateContractViewModel.currentContractId
import com.fagougou.government.generateContract.GenerateContractViewModel.getGenerateForm
import com.fagougou.government.generateContract.GenerateContractViewModel.getGenerateTemplate
import com.fagougou.government.CommonApplication.Companion.presentation
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.UMConstans
import com.umeng.analytics.MobclickAgent
import kotlinx.coroutines.launch

@Composable
fun GenerateGuide(navController: NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        presentation?.playVideo(R.raw.vh_generate_guide)
        val launchId = contractList.firstOrNull()?.id ?: return@LaunchedEffect
        GenerateContractViewModel.clear()
        currentContractId.value = launchId
        scope.launch {
            getGenerateTemplate(launchId)
            GenerateContractViewModel.updateContent()
        }
        scope.launch { getGenerateForm(launchId) }
    }
    Surface(color = Color.White) {
        Column(Modifier.fillMaxSize(),Arrangement.Top,Alignment.CenterHorizontally) {
            Header("智能文书",navController,{ GenerateContractViewModel.clear() })
            Row(
                Modifier
                    .fillMaxWidth(0.88f)
                    .padding(vertical = 12.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                for (item in contractList) {
                    Surface(
                        Modifier
                            .height(64.dp)
                            .width(224.dp)
                            .padding(end = 16.dp)) {
                        if(item.id == currentContractId.value) Row(
                            Modifier.fillMaxSize(),
                            Arrangement.End
                        ){
                            Image(painterResource(R.drawable.ic_blue_select),null)
                        }
                        Row(
                            Modifier
                                .fillMaxSize()
                                .border(
                                    1.dp,
                                    if (item.id == currentContractId.value) Dodgerblue else Color.LightGray,
                                    RoundedCornerShape(12)
                                )
                                .clickable {
                                    GenerateContractViewModel.clear()
                                    currentContractId.value = item.id
                                    scope.launch {
                                        getGenerateTemplate(item.id)
                                        GenerateContractViewModel.updateContent()
                                    }
                                    scope.launch { getGenerateForm(item.id) }
                                },
                            Arrangement.Center,
                            Alignment.CenterVertically,
                        ){
                            Image(painterResource(R.drawable.ic_word), null )
                            Spacer(
                                Modifier
                                    .height(16.dp)
                                    .width(16.dp))
                            Text(item.name,fontSize = 18.sp)
                        }
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.88f),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(750.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(CORNER_FLOAT)),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ){
                    GenerateWebView()
                }
                Row(
                    Modifier.fillMaxSize(),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ){
                    Button(
                        {
                            navController.navigate(Router.generateContract)
                        },
                        Modifier.height(60.dp).width(180.dp),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp),
                        colors = buttonColors(backgroundColor = Dodgerblue),
                        shape = RoundedCornerShape(16)
                    ){
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_edit),null)
                            Text(
                                "填写模板",
                                Modifier.padding(start = 16.dp),
                                color = Color.White,
                                fontSize = 21.sp)
                        }
                    }
                    Spacer(
                        Modifier
                            .width(32.dp)
                            .height(32.dp))
                    Button(
                        { DialogViewModel.confirmPrint() },
                        Modifier.height(60.dp).width(180.dp),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp),
                        colors = buttonColors(backgroundColor = Dodgerblue),
                        shape = RoundedCornerShape(16)
                    ){
                        Row( verticalAlignment = Alignment.CenterVertically ){
                            Image(painterResource(R.drawable.ic_painter),null)
                            Text(
                                "打印合同",
                                Modifier.padding(start = 16.dp),
                                color = Color.White,
                                fontSize = 21.sp)
                        }
                    }
                }
            }
        }
    }
}
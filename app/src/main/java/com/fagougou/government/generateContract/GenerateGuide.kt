package com.fagougou.government.generateContract

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
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
import com.fagougou.government.component.Header
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.generateContract.GenerateContract.contractList
import com.fagougou.government.generateContract.GenerateContract.currentContractId
import com.fagougou.government.generateContract.GenerateContract.data
import com.fagougou.government.generateContract.GenerateContract.getGenerateForm
import com.fagougou.government.generateContract.GenerateContract.getGenerateTemplate
import com.fagougou.government.generateContract.GenerateContract.printRequest
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import kotlinx.coroutines.launch

@Composable
fun GenerateGuide(navController: NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        val launchId = contractList.firstOrNull()?.id ?: return@LaunchedEffect
        GenerateContract.clear()
        currentContractId.value = launchId
        scope.launch {
            getGenerateTemplate(launchId)
            GenerateContract.updateContent()
        }
        scope.launch { getGenerateForm(launchId) }
    }
    Surface(color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                title = "智能文书",
                navController = navController,
                onBack = { GenerateContract.clear() }
            )
            Row(
                Modifier
                    .fillMaxWidth(0.88f)
                    .padding(vertical = 12.dp)
            ) {
                for (item in contractList) {
                    Surface(
                        Modifier
                            .height(64.dp)
                            .width(192.dp)) {
                        if(item.id == currentContractId.value) Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.End
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
                                    GenerateContract.clear()
                                    currentContractId.value = item.id
                                    scope.launch {
                                        getGenerateTemplate(item.id)
                                        GenerateContract.updateContent()
                                    }
                                    scope.launch { getGenerateForm(item.id) }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(750.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(CORNER_FLOAT)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    ContractWebView(data)
                }
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        modifier = Modifier
                            .height(60.dp)
                            .width(180.dp),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp),
                        onClick = {
                            navController.navigate(Router.generateContract)
                        },
                        content = {
                            Row( verticalAlignment = Alignment.CenterVertically ){
                                Image(painterResource(R.drawable.ic_edit),null)
                                Text(
                                    modifier = Modifier.padding(start = 16.dp),
                                    text = "填写合同",
                                    color = Color.White,
                                    fontSize = 21.sp)
                            }
                        },
                        colors = buttonColors(backgroundColor = Dodgerblue),
                        shape = RoundedCornerShape(16)
                    )
                    Spacer(Modifier.width(32.dp).height(32.dp))
                    Button(
                        modifier = Modifier
                            .height(60.dp)
                            .width(180.dp),
                        elevation = ButtonDefaults.elevation(0.dp,0.dp),
                        onClick = { DialogViewModel.startPrint(scope) },
                        content = {
                            Row( verticalAlignment = Alignment.CenterVertically ){
                                Image(painterResource(R.drawable.ic_painter),null)
                                Text(
                                    modifier = Modifier.padding(start = 16.dp),
                                    text = "打印合同",
                                    color = Color.White,
                                    fontSize = 21.sp)
                            }
                        },
                        colors = buttonColors(backgroundColor = Dodgerblue),
                        shape = RoundedCornerShape(16)
                    )
                }
            }
        }
    }
}
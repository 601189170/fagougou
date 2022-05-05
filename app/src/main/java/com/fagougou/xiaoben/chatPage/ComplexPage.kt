package com.fagougou.xiaoben.chatPage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.chatPage.Complex.bodyList
import com.fagougou.xiaoben.chatPage.Complex.caseList
import com.fagougou.xiaoben.chatPage.Complex.selectPage
import com.fagougou.xiaoben.model.AttachmentBody
import com.fagougou.xiaoben.model.AttachmentCases
import com.fagougou.xiaoben.model.CaseResponse
import com.fagougou.xiaoben.repo.ApiService
import com.fagougou.xiaoben.repo.Client.apiService
import com.fagougou.xiaoben.ui.theme.CORNER_FLOAT
import com.fagougou.xiaoben.ui.theme.Dodgerblue
import com.fagougou.xiaoben.webViewPage.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Complex{
    val selectPage = mutableStateOf("body")
    var bodyList = mutableStateListOf<AttachmentBody>()
    val caseList = mutableStateListOf<AttachmentCases>()
}

@Composable
fun CaseButton(case: AttachmentCases,navController: NavController){
    Button(
        modifier = Modifier.padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        elevation = ButtonDefaults.elevation(0.dp),
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .border(2.dp, Color(0x33000000), RoundedCornerShape(CORNER_FLOAT))
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(0.97f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold,
                        text = case.number + case.name,
                        fontSize = 28.sp,
                        color = Color.Black
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(0.94f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        maxLines=1,
                        text = case.date,
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                    Text(
                        maxLines=1,
                        modifier = Modifier.padding(start = 8.dp),
                        text = case.court,
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.case(case.serial).execute()
                val body = response.body() ?: CaseResponse()
                Case.data = body.data
                withContext(Dispatchers.Main){
                    navController.navigate("case")
                }
            }
        }
    )
}

@Composable
fun ComplexPage(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column {
            Headder("详细分析", navController, onBack = {selectPage.value = "body"})
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if(bodyList.isNotEmpty())Button(
                    onClick = { selectPage.value = "body" },
                    content = { Text("法律建议", color = Dodgerblue) },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    border = BorderStroke(2.dp, Dodgerblue)
                )
                if(caseList.isNotEmpty())Button(
                    onClick = { selectPage.value = "case" },
                    content = { Text("判决案例", color = Dodgerblue) },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    border = BorderStroke(2.dp, Dodgerblue)
                )
            }
            Column(modifier = Modifier.verticalScroll(ScrollState(0))) {
                when (selectPage.value) {
                    "body" -> for(body in bodyList)if(body.content != "")WebView("", body.content)
                    "case" -> for(case in caseList) CaseButton(case,navController)
                }
            }
        }
    }
}
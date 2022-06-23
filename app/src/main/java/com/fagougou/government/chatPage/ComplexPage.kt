package com.fagougou.government.chatPage

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.government.component.Header
import com.fagougou.government.Router
import com.fagougou.government.chatPage.Complex.bodyList
import com.fagougou.government.chatPage.Complex.caseList
import com.fagougou.government.chatPage.Complex.selectPage
import com.fagougou.government.model.AttachmentBody
import com.fagougou.government.model.AttachmentCases
import com.fagougou.government.model.AttachmentResponse
import com.fagougou.government.model.CaseResponse
import com.fagougou.government.repo.Client.apiService
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.webViewPage.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Complex{
    val selectPage = mutableStateOf("body")
    var bodyList = mutableStateListOf<AttachmentBody>()
    val caseList = mutableStateListOf<AttachmentCases>()

    fun clear(){
        selectPage.value = "body"
        bodyList.clear()
        caseList.clear()
    }
}

@Composable
fun CaseButton(case: AttachmentCases,scope: CoroutineScope,navController: NavController){
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
            scope.launch(Dispatchers.IO) {
                val response = apiService.case(case.serial).execute()
                val body = response.body() ?: CaseResponse()
                body.data.DocContent = body.data.DocContent.replace("\n","<br>")
                Case.data = body.data
                withContext(Dispatchers.Main){
                    navController.navigate(Router.case)
                }
            }
        }
    )
}

@Composable
fun ComplexPage(navController: NavController) {
    val scope = rememberCoroutineScope()
    Surface(Modifier.fillMaxSize(), color = Color.White) {
        Column {
            Header("详细分析", navController, onBack = { Complex.clear() } )
            Row(Modifier.fillMaxWidth().padding(vertical = 24.dp), Arrangement.SpaceEvenly) {
                if(bodyList.isNotEmpty() && caseList.isNotEmpty()){
                    Button(
                        { selectPage.value = "body" },
                        content = { Text("法律建议", color = if(selectPage.value == "body")Dodgerblue else Color.Gray) },
                        colors = ButtonDefaults.buttonColors(Color.White),
                        border = BorderStroke(2.dp, if(selectPage.value == "body")Dodgerblue else Color.Gray)
                    )
                    Button(
                        { selectPage.value = "case" },
                        content = { Text("判决案例", color = if(selectPage.value == "case")Dodgerblue else Color.Gray) },
                        colors = ButtonDefaults.buttonColors(Color.White),
                        border = BorderStroke(2.dp, if(selectPage.value == "case")Dodgerblue else Color.Gray)
                    )
                }
            }
            Column(modifier = Modifier.verticalScroll(ScrollState(0))) {
                when (selectPage.value) {
                    "body" -> for(body in bodyList) if(body.content != "")WebView("", body.content.replace("\n",""))
                    "case" -> for(case in caseList) CaseButton(case,scope,navController)
                }
            }
        }
    }
}
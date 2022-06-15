package com.fagougou.government.chatPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.chatPage.RePortMainModel.cases
import com.fagougou.government.model.AttachmentCases
import com.fagougou.government.model.CaseResponse
import com.fagougou.government.repo.Client
import com.fagougou.government.ui.theme.CORNER_FLOAT8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CasesPage (navController: NavController){
    val scope = rememberCoroutineScope()
    LazyColumn(
        Modifier.padding(top = 16.dp,start = 60.dp,end = 60.dp),
        horizontalAlignment= Alignment.CenterHorizontally
    ) {
        items(cases){
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable { getCaseSeria(it,scope,navController) }
                .padding(top = 24.dp)
                .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8))
                ,contentAlignment = Alignment.CenterStart) {
                Row(verticalAlignment= Alignment.CenterVertically ,
                    modifier = Modifier.padding(top = 32.dp, start = 32.dp, bottom = 32.dp)) {
                    Image(painterResource(R.drawable.ic_icon_aboutcase), null)
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = it.name,
                            fontSize = 24.sp,
                            color = Color(0xFF222222)
                        )
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "日期："+it.date+"  "+it.court,
                            fontSize = 20.sp,
                            color = Color(0xFF909499)
                        )
                    }
                }
            }
        }

    }

}


fun getCaseSeria(case: AttachmentCases,scope: CoroutineScope,navController: NavController){
    scope.launch(Dispatchers.IO) {
        val response = Client.apiService.case(case.serial).execute()
        val body = response.body() ?: CaseResponse()
        body.data.DocContent = body.data.DocContent.replace("\n","<br>")
        Case.data = body.data
        withContext(Dispatchers.Main){
            navController.navigate(Router.case)
        }
    }
}
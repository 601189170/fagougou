package com.fagougou.government.chatPage


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.chatPage.RePortMainModel.cases
import com.fagougou.government.chatPage.RePortMainModel.content
import com.fagougou.government.chatPage.RePortMainModel.laws
import com.fagougou.government.chatPage.RePortMainModel.setTab
import com.fagougou.government.chatPage.RePortMainModel.stepdata
import com.fagougou.government.chatPage.RePortMainModel.sugdata
import com.fagougou.government.chatPage.RePortMainModel.title
import com.fagougou.government.chatPage.RePortMainModel.toplist
import com.fagougou.government.chatPage.RePortMainModel.type

import com.fagougou.government.component.Header
import com.fagougou.government.model.AttachmentCases
import com.fagougou.government.model.AttachmentContent
import com.fagougou.government.model.AttachmentLaws
import com.fagougou.government.model.AttachmentResponse
import com.fagougou.government.ui.theme.CORNER_FLOAT8

object RePortMainModel {
    var setTab=mutableStateOf(0)
    var stepdata=""
    var sugdata=""
    var type=""
    var title=""
    var content: AttachmentContent?=null
    var cases= mutableStateListOf<AttachmentCases>()
    var laws= mutableStateListOf<AttachmentLaws>()
    var toplist=listOf<String>()
}

@Composable
fun RePortMain (navController: NavController){
    val navController2 = rememberNavController()
    Column(Modifier.fillMaxSize()) {
    Header(title = title+"分析报告", navController = navController)
    Surface(Modifier.fillMaxSize(),color = Color.White) {
        Row(Modifier.fillMaxSize() ) {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp, start = 2.dp, end = 2.dp, bottom = 2.dp)
                    .fillMaxSize(0.3f),
                horizontalAlignment=Alignment.CenterHorizontally) {

                    Box(modifier = Modifier
                        .clickable {
                            setTab.value = 0
                            navController2.navigate(Router.reportpage)
                        }
                        .padding(top = 24.dp)
                        .width(200.dp)
                        .height(64.dp)
                        .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8)),
                        contentAlignment = Alignment.Center) {
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .padding()
                                .background(Color(if (setTab.value == 0) 0xFF0F87FF else 0xFFFFFFF))
                            ,verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center) {
                            Image(painterResource(if (setTab.value==0) R.drawable.ic_icon_tab1_true else R.drawable.ic_icon_tab1_false), null)
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                fontWeight = FontWeight.Bold,
                                text = "分析报告",
                                fontSize = 24.sp,
                                color = Color(if (setTab.value == 0) 0xFFFFFFFF else 0xFF222222)
                            )
                        }

                    }
                if (!cases.isNullOrEmpty()) {
                    Box(modifier = Modifier
                        .clickable {
                            setTab.value = 1
                            navController2.navigate(Router.casepage)
                        }
                        .padding(top = 24.dp)
                        .width(200.dp)
                        .height(64.dp)
                        .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8)),
                        contentAlignment = Alignment.Center) {
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .padding()
                                .background(Color(if (setTab.value == 1) 0xFF0F87FF else 0xFFFFFFF)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painterResource(if (setTab.value == 1) R.drawable.ic_icon_tab2_true else R.drawable.ic_icon_tab2_false),
                                null
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "相关案例",
                                fontSize = 24.sp,
                                color = Color(if (setTab.value == 1) 0xFFFFFFFF else 0xFF222222)
                            )
                        }
                    }
                }
                if (!laws.isNullOrEmpty()) {
                    Box(modifier = Modifier
                        .clickable {
                            setTab.value = 2
                            navController2.navigate(Router.lawspage)
                        }
                        .padding(top = 24.dp)
                        .width(200.dp)
                        .height(64.dp)
                        .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8)),
                        contentAlignment = Alignment.Center) {
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .padding()
                                .background(Color(if (setTab.value == 2) 0xFF0F87FF else 0xFFFFFFF)),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painterResource(if (setTab.value == 2) R.drawable.ic_icon_tab3_true else R.drawable.ic_icon_tab3_false),
                                null
                            )
                            Text(

                                modifier = Modifier.padding(start = 8.dp),
                                text = "法律依据",
                                fontSize = 24.sp,
                                color = Color(if (setTab.value == 2) 0xFFFFFFFF else 0xFF222222)
                            )
                        }
                    }
                }

            }
            Surface(
                Modifier
                    .width(1.dp)
                    .fillMaxHeight(),color = Color.LightGray) {}
            Surface(
                Modifier
                    .fillMaxSize()
                ,color = Color.White) {
                Column( Modifier.fillMaxSize(), Arrangement.Top, Alignment.CenterHorizontally ) {
                    NavHost(navController2, Router.reportpage, Modifier.fillMaxHeight()) {
                        composable(Router.lawspage) { LawsPage(navController) }
                        composable(Router.casepage) { CasesPage(navController) }
                        composable(Router.reportpage) { Report(navController) }
                    }
                }

            }
        }
        }
}
}

fun setReportData(bean:AttachmentResponse){
    content=bean.data.content
    type=bean.data.content.chart.type
    title=bean.data.title
    toplist= content?.contexts!!
    cases.addAll(bean.data.content.cases)
    laws.addAll(bean.data.content.laws)
    content?.body?.forEach(){
        if (it.title.contains("行动建议")||it.title.contains("流程")){ stepdata=it.content }
        if (it.title.contains("专家")){ sugdata=it.content }
    }

}















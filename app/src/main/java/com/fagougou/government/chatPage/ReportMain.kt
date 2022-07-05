package com.fagougou.government.chatPage

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
import com.fagougou.government.chatPage.ReportMainModel.cases
import com.fagougou.government.chatPage.ReportMainModel.content
import com.fagougou.government.chatPage.ReportMainModel.laws
import com.fagougou.government.chatPage.ReportMainModel.setTab
import com.fagougou.government.chatPage.ReportMainModel.stepdata
import com.fagougou.government.chatPage.ReportMainModel.sugdata
import com.fagougou.government.chatPage.ReportMainModel.title
import com.fagougou.government.chatPage.ReportMainModel.toplist
import com.fagougou.government.chatPage.ReportMainModel.type
import com.fagougou.government.component.Header
import com.fagougou.government.model.AttachmentCases
import com.fagougou.government.model.AttachmentContent
import com.fagougou.government.model.AttachmentLaws
import com.fagougou.government.model.AttachmentResponse
import com.fagougou.government.ui.theme.CORNER_FLOAT8

object ReportMainModel {
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
fun ReportMain (navController: NavController){
    val navController2 = rememberNavController()
    Column(Modifier.fillMaxSize()) {
        Header(title, navController)
        Surface(Modifier.fillMaxSize(),color = Color.White) {
            Row(Modifier.fillMaxSize() ) {
                Column(
                    Modifier
                        .padding(top = 20.dp, start = 2.dp, end = 2.dp, bottom = 2.dp)
                        .fillMaxSize(0.3f),
                    horizontalAlignment=Alignment.CenterHorizontally
                ) {
                    Box(Modifier
                        .padding(top = 24.dp)
                        .width(200.dp)
                        .height(64.dp)
                        .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8)),
                        contentAlignment = Alignment.Center) {
                        Row(
                            Modifier
                                .clickable {
                                    setTab.value = 0
                                    navController2.navigate(Router.reportPage) }
                                .fillMaxSize()
                                .padding(2.dp)
                                .padding()
                                .background(Color(if (setTab.value == 0) 0xFF0F87FF else 0xFFFFFFF)),
                            Arrangement.Center,
                            Alignment.CenterVertically
                        ) {
                            Image(painterResource(if (setTab.value==0) R.drawable.ic_icon_tab1_true else R.drawable.ic_icon_tab1_false), null)
                            Text(
                                "分析报告",
                                Modifier.padding(start = 8.dp),
                                Color(if (setTab.value == 0) 0xFFFFFFFF else 0xFF222222),
                                24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                    if (!cases.isNullOrEmpty()) {
                        Box(
                            Modifier
                                .padding(top = 24.dp)
                                .width(200.dp)
                                .height(64.dp)
                                .border(1.dp, Color(0xFFEBEDF0),
                            RoundedCornerShape(CORNER_FLOAT8)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                                    .padding()
                                    .background(Color(if (setTab.value == 1) 0xFF0F87FF else 0xFFFFFFF))
                                    .clickable {
                                        setTab.value = 1
                                        navController2.navigate(Router.casePage)
                                    },
                                Arrangement.Center,
                                Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(if (setTab.value == 1) R.drawable.ic_icon_tab2_true else R.drawable.ic_icon_tab2_false),
                                    null
                                )
                                Text(
                                    "相关案例",
                                    Modifier.padding(start = 8.dp),
                                    Color(if (setTab.value == 1) 0xFFFFFFFF else 0xFF222222),
                                    24.sp,
                                )
                            }
                        }
                    }
                    if (!laws.isNullOrEmpty()) {
                        Box(
                            Modifier
                                .padding(top = 24.dp)
                                .width(200.dp)
                                .height(64.dp)
                                .border(1.dp, Color(0xFFEBEDF0),
                            RoundedCornerShape(CORNER_FLOAT8)),
                            contentAlignment = Alignment.Center) {
                            Row(
                                Modifier
                                    .clickable {
                                        setTab.value = 2
                                        navController2.navigate(Router.lawsPage) }
                                    .fillMaxSize()
                                    .padding(2.dp)
                                    .padding()
                                    .background(Color(if (setTab.value == 2) 0xFF0F87FF else 0xFFFFFFF)),
                                Arrangement.Center,
                                Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(if (setTab.value == 2) R.drawable.ic_icon_tab3_true else R.drawable.ic_icon_tab3_false),
                                    null
                                )
                                Text(
                                    "法律依据",
                                    Modifier.padding(start = 8.dp),
                                    Color(if (setTab.value == 2) 0xFFFFFFFF else 0xFF222222),
                                    24.sp,
                                )
                            }
                        }
                    }

                }
                Surface(
                    Modifier.width(1.dp).fillMaxHeight(),
                    color = Color.LightGray
                ) {}
                Surface(
                    Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    Column( Modifier.fillMaxSize(), Arrangement.Top, Alignment.CenterHorizontally ) {
                        NavHost(navController2, Router.reportPage, Modifier.fillMaxHeight()) {
                            composable(Router.lawsPage) { LawsPage(navController) }
                            composable(Router.casePage) { CasesPage(navController) }
                            composable(Router.reportPage) { Report(navController) }
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
    content?.body?.forEach{
        if (it.title.contains("行动建议")||it.title.contains("流程")){ stepdata=it.content }
        if (it.title.contains("专家")){ sugdata=it.content }
    }
}

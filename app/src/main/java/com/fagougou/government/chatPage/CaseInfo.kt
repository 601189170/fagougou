package com.fagougou.government.chatPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

import com.fagougou.government.chatPage.CaseInfoModel.isExpen
import com.fagougou.government.chatPage.CaseInfoModel.setTab
import com.fagougou.government.component.Header
import com.fagougou.government.model.ContractCategory
import com.fagougou.government.model.ContractData
import com.fagougou.government.ui.theme.CORNER_FLOAT8

object CaseInfoModel {
    val categoryList = mutableStateListOf<ContractCategory>()
    val ContractLists = mutableStateListOf<ContractData>()

    var isExpen= mutableStateOf(false)
    var setTab=mutableStateOf(1)
}

@Composable
fun CaseInfo (navController: NavController){

    val navController2 = rememberNavController()

    Column(Modifier.fillMaxSize()) {

    Header(title = "分析报告", navController = navController)
    Surface(Modifier.fillMaxSize(),color = Color.White) {

        Row(Modifier.fillMaxSize() ) {
            Column(
                modifier = Modifier

                    .padding(top = 20.dp,start = 2.dp,end = 2.dp,bottom = 2.dp)
                    .fillMaxSize(0.3f),
                horizontalAlignment=Alignment.CenterHorizontally) {

                    Box(modifier = Modifier
                        .clickable { setTab.value = 0
                            navController2.navigate(Router.Report)}
                        .padding(top=24.dp)
                        .width(200.dp)
                        .height(64.dp)
                        .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8)),
                        contentAlignment = Alignment.Center) {
                        Row(Modifier.fillMaxSize().padding(2.dp).padding()
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
                    Box(modifier = Modifier
                        .clickable {
                            setTab.value = 1
                            navController2.navigate(Router.aboutcase)
                        }
                        .padding(top = 24.dp)
                        .width(200.dp)
                        .height(64.dp)
                        .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8)),
                        contentAlignment = Alignment.Center) {
                        Row(Modifier.fillMaxSize().padding(2.dp).padding()
                            .background(Color(if (setTab.value == 1) 0xFF0F87FF else 0xFFFFFFF))
                            ,verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center){
                            Image(painterResource(if (setTab.value==1) R.drawable.ic_icon_tab2_true else R.drawable.ic_icon_tab2_false), null)
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = "相关案例",
                                fontSize = 24.sp,
                                color = Color(if (setTab.value == 1) 0xFFFFFFFF else 0xFF222222)
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .clickable {
                            setTab.value = 2
                            navController2.navigate(Router.rulebase)
                        }
                        .padding(top = 24.dp)
                        .width(200.dp)
                        .height(64.dp)
                        .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8)),
                        contentAlignment = Alignment.Center) {
                        Row(Modifier.fillMaxSize().padding(2.dp).padding()
                            .background(Color(if (setTab.value == 2) 0xFF0F87FF else 0xFFFFFFF))
                            ,verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center) {
                            Image(painterResource(if (setTab.value==2) R.drawable.ic_icon_tab3_true else R.drawable.ic_icon_tab3_false), null)
                            Text(

                                modifier = Modifier.padding(start = 8.dp),
                                text = "法律依据",
                                fontSize = 24.sp,
                                color = Color(if (setTab.value == 2) 0xFFFFFFFF else 0xFF222222)
                            )
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
                    NavHost(navController2, Router.rulebase, Modifier.fillMaxHeight()) {
                        composable(Router.rulebase) { RuleBase() }
                        composable(Router.aboutcase) { AboutCase() }
                        composable(Router.Report) { Report() }
                    }
                }

            }
        }


        }
}
}







@Composable
fun AboutCase (){
    val categoryList = arrayListOf<String>("郑瑞英与黄绍飞离婚纠纷一审民事判决","郑瑞英与黄绍飞离婚纠纷一审民事判决","郑瑞英与黄绍飞离婚纠纷一审民事判决")




    LazyColumn(
        Modifier.padding(top = 16.dp,start = 60.dp,end = 60.dp),
        horizontalAlignment=Alignment.CenterHorizontally
    ) {
        items(categoryList){
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable { }
                .padding(top = 24.dp)
                .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8))
                ,contentAlignment = Alignment.CenterStart) {
                Row(verticalAlignment=Alignment.CenterVertically ,
                    modifier = Modifier.padding(top = 32.dp, start = 32.dp, bottom = 32.dp)) {
                    Image(painterResource(R.drawable.ic_icon_aboutcase), null)
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = it,
                            fontSize = 24.sp,
                            color = Color(0xFF222222)
                        )
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "日期："+"2015-07-07"+"  "+"广东省深圳市南山区人民法院",
                            fontSize = 20.sp,
                            color = Color(0xFF909499)
                        )
                    }
                }
            }
        }

    }

}






@Composable
fun RuleBase (){
    val categoryList = arrayListOf<String>("中华人民共和国婚姻法","中华人民共和国婚姻法","中华人民共和国婚姻法")

    LazyColumn(
        Modifier.padding(top = 16.dp,start = 60.dp,end = 60.dp),
        horizontalAlignment=Alignment.CenterHorizontally
    ) {
        items(categoryList){
            Box( modifier = Modifier
                .padding(top = 24.dp)
                .clickable { if (!isExpen.value) isExpen.value = true else isExpen.value = false }
                .border(1.dp, Color(0xFFEBEDF0), RoundedCornerShape(CORNER_FLOAT8))
                ,contentAlignment = Alignment.Center){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp)
                ) {
                    Row(
                        horizontalArrangement=Arrangement.SpaceBetween,
                        verticalAlignment=Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp, start = 32.dp, bottom = 32.dp, end = 32.dp)) {

                        Column() {
                            Text(
                                fontWeight = FontWeight.Bold,
                                text = it,
                                fontSize = 24.sp,
                                color = Color(0xFF222222)
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp),
                                text = "第二十条",
                                fontSize = 20.sp,
                                color = Color(0xFF0F87FF)
                            )
                        }
                        Image(painterResource(if (!isExpen.value) R.drawable.ic_icon_down else R.drawable.ic_icon_up), null)

                    }
                    if (isExpen.value){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ){
                            Text(

                                text = "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 32.dp, bottom = 32.dp, end = 24.dp),
                                color = Color(0xFF666666),
                                fontSize = 20.sp
                            )

                        }
                    }


                }
            }

        }
    }
}
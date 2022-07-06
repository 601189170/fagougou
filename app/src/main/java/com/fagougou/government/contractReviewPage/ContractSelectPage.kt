@file:JvmName("ContractSelectMainKt")

package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.government.Router
import com.fagougou.government.chatPage.CasesPage
import com.fagougou.government.chatPage.LawsPage
import com.fagougou.government.chatPage.Report

import com.fagougou.government.component.Header
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.component.VerticalGrid
import com.fagougou.government.homePage.HomeButton
import com.fagougou.government.model.StepModel
import com.fagougou.government.webViewPage.WebViewPageModel

@Composable
fun ContractSelectPage(navController: NavController) {


    Column(
        Modifier.fillMaxSize(),
    ) { Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xF3F3F3F3)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(

                    modifier = Modifier.padding( top = 40.dp),
                    text = "请选择合同类型",
                    fontSize = 28.sp,
                    color = Color(0xFF303133)
                )
                LazyVerticalGrid(

                    userScrollEnabled = false,
                    contentPadding=PaddingValues(30.dp),
                    modifier = Modifier
                        .padding(horizontal = 56.dp)
                        .padding(top = 24.dp),
                    columns = GridCells.Fixed(5),
                    content = {
                        items(10){ cal ->
                            Column(
                                Modifier
                                    .clickable { navController.navigate(Router.upload) }
                                    .height(240.dp)
                                    .width(208.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column( Modifier.background(Color.White),horizontalAlignment = Alignment.CenterHorizontally) {

                                    Box(
                                        Modifier
                                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                            .width(185.dp)
                                            .height(156.dp)
                                            .background(Color(0xFFEFF5FC))) {


                                    }
                                    Text(
                                        modifier = Modifier.padding(top = 15.dp,),
                                        text = "通用合同",
                                        fontSize = 20.sp,
                                        color = Color(0xFF303133)
                                    )

                                    Spacer(modifier = Modifier.height(15.dp))

                                }





                            }

                        }
                    }
                )


            }




    }
}
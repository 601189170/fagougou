package com.fagougou.xiaoben.calculatorPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.homePage.HomeButton
import com.fagougou.xiaoben.webViewPage.WebViewModel.WebViewUrl

@Composable
fun CalculatorGuidePage(navController: NavController) {
    val calResMap = mapOf(
        Pair("律师费", R.drawable.cal_lawyer),
        Pair("诉讼费", R.drawable.cal_litigation),
        Pair("司法鉴定", R.drawable.cal_judicial),
        Pair("逾期利息", R.drawable.cal_interest),
        Pair("公证费", R.drawable.cal_notary),
        Pair("违约金", R.drawable.cal_damage),
        Pair("仲裁费", R.drawable.cal_arbitration),
    )
    val calUrlMap = mapOf(
        Pair("律师费", "https://www.fagougou.com/homepage/m/legalTools/Legalfee.html"),
        Pair("诉讼费", "https://www.fagougou.com/homepage/m/legalTools/Litigation.html"),
        Pair("司法鉴定", "https://www.fagougou.com/homepage/m/legalTools/Attorneys.html"),
        Pair("逾期利息", "https://www.fagougou.com/homepage/m/legalTools/OverdueInterest.html"),
        Pair("公证费", "https://www.fagougou.com/homepage/m/legalTools/Notarial.html"),
        Pair("违约金", "https://www.fagougou.com/homepage/m/legalTools/Breach.html"),
        Pair("仲裁费", "https://www.fagougou.com/homepage/m/legalTools/Arbitration.html"),
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Headder(title = "法律计算器", navController = navController)
        Text(
            modifier = Modifier.padding(top = 96.dp),
            text = "法律计算器",
            fontSize = 45.sp,
            color = Color.White
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = "快速智能，助您计算相关费用",
            fontSize = 28.sp,
            color = Color.White
        )
        LazyVerticalGrid(
            userScrollEnabled = false,
            modifier = Modifier.padding(48.dp),
            columns = GridCells.Fixed(2),
            content = {
                items(calResMap.toList()){ cal ->
                    Column(modifier= Modifier.fillMaxSize().height(184.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                        HomeButton(
                            modifier= Modifier
                                .width(472.dp)
                                .height(160.dp),
                            onClick = {
                                WebViewUrl = calUrlMap[cal.first] ?: ""
                                navController.navigate("WebView")
                            },
                            contentId = cal.second
                        )
                    }
                }
            }
        )
    }
}
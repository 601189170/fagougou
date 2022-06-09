package com.fagougou.government.calculatorPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.homePage.HomeButton
import com.fagougou.government.presentation.BannerPresentation.Companion.mediaPlayer
import com.fagougou.government.webViewPage.WebViewPageModel

@Composable
fun CalculatorGuidePage(navController: NavController) {
    LaunchedEffect(null){
        mediaPlayer.stop()
        mediaPlayer.seekTo(0)
        mediaPlayer.setDataSource(CommonApplication.activity.resources.openRawResourceFd(R.raw.vh_calculator))
        mediaPlayer.prepareAsync()
    }
    val calResMap = mapOf(
        Pair("律师费", R.drawable.cal_lawyer),
        Pair("司法鉴定", R.drawable.cal_judicial),
        Pair("公证费", R.drawable.cal_notary),
        Pair("诉讼费", R.drawable.cal_litigation),
        Pair("逾期利息", R.drawable.cal_interest),
        Pair("违约金", R.drawable.cal_damage),
        Pair("仲裁费", R.drawable.cal_arbitration),
    )
    val calUrlMap = mapOf(
        Pair("律师费", "https://www.fagougou.com/homepage/m/legalTools/Legalfee.html"),
        Pair("司法鉴定", "https://www.fagougou.com/homepage/m/legalTools/Attorneys.html"),
        Pair("公证费", "https://www.fagougou.com/homepage/m/legalTools/Notarial.html"),
        Pair("诉讼费", "https://www.fagougou.com/homepage/m/legalTools/Litigation.html"),
        Pair("逾期利息", "https://www.fagougou.com/homepage/m/legalTools/OverdueInterest.html"),
        Pair("违约金", "https://www.fagougou.com/homepage/m/legalTools/Breach.html"),
        Pair("仲裁费", "https://www.fagougou.com/homepage/m/legalTools/Arbitration.html"),
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(title = "法律计算器", navController = navController)
        BasicText("法律计算器",96.dp,32.sp)
        BasicText("快速智能，助您计算相关费用",8.dp,24.sp)
        LazyVerticalGrid(
            userScrollEnabled = false,
            modifier = Modifier.padding(start = 100.dp,end = 100.dp),
            columns = GridCells.Fixed(3),
            content = {
                items(calResMap.toList()){ cal ->
                    Column(modifier= Modifier
                        .fillMaxSize()
                        .height(160.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                        HomeButton(
                            modifier= Modifier
                                .width(320.dp)
                                .height(120.dp),
                            onClick = {
                                WebViewPageModel.title = "法律计算器"
                                WebViewPageModel.data = ""
                                WebViewPageModel.urlAddress = calUrlMap[cal.first] ?: ""
                                navController.navigate(Router.webView)
                            },
                            contentId = cal.second
                        )
                    }
                }
            }
        )
    }
}
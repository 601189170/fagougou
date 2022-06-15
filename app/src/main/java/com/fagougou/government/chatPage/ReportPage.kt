package com.fagougou.government.chatPage

import android.app.slice.Slice
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.fagougou.government.chatPage.RePortMainModel.content
import com.fagougou.government.chatPage.RePortMainModel.stepdata
import com.fagougou.government.chatPage.RePortMainModel.sugdata
import com.fagougou.government.chatPage.RePortMainModel.toplist
import com.fagougou.government.chatPage.RePortMainModel.type
import com.fagougou.government.homePage.HomeButton
import com.fagougou.government.model.AttachmentContent
import com.fagougou.government.view.PieChartDataModel
import com.fagougou.government.webViewPage.WebView
import com.fagougou.government.webViewPage.WebViewPageModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Report (navController: NavController){

    Column(
        Modifier
            .verticalScroll(ScrollState(0))
            .fillMaxSize()
            .padding(horizontal = 100.dp)) {
        if (toplist.isNotEmpty()) topListBox()
        if (type=="pie") content?.let { chartBox(it) }
        if (!stepdata.isBlank()) stepBox()
        if (!sugdata.isBlank()) sugBox()
    }
}





@Composable
private fun PieChartRow(float1: Float,float2: Float,pieChartDataModel: PieChartDataModel) {
        PieChart(
            pieChartData = PieChartData(
                slices = listOf(
                    PieChartData.Slice(float1, Color(0XFF3C8EED)),
                    PieChartData.Slice(float2, Color(0XFFF6C500))
                )
            ),
            sliceDrawer = SimpleSliceDrawer(
                sliceThickness = pieChartDataModel.sliceThickness
            )
        )

}
@Composable
fun topListBox() {

    Column(
        Modifier
            .height(150.dp)
    ) {

            LazyHorizontalGrid( rows = GridCells.Fixed(1),contentPadding=PaddingValues(20.dp),userScrollEnabled = false,
                content = {
                    items(toplist) { bot ->
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                modifier = Modifier
                                    .background(Color(0xFFECF5FF))
                                    .padding(20.dp),
                                text = bot,
                                fontSize = 20.sp,
                                color = Color(0xFF0E7AE6)
                            )
                        }
                    }
                })



        Surface(
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(1.dp), color = Color.Gray
        ) {

        }
    }
}

@Composable
fun chartBox(data: AttachmentContent){
    if (data.chart.data.isNotEmpty()) {
        Column(Modifier.padding(top = 40.dp)) {
            Image(painterResource(R.drawable.ic_icon_rep1), null)

            Text(
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp),
                text =  "根据案情，法院支持您"+data.chart.title+"为:",
                fontSize = 24.sp,
                color = Color(0xFF222222)
            )
            var ft1 = data.chart.data.get(0).value
            var ft2 = data.chart.data.get(1).value
            Box(Modifier.fillMaxWidth().padding(top = 10.dp), contentAlignment = Alignment.Center) {
                val pieChartDataModel = remember { PieChartDataModel() }
                PieChartRow(ft1 * 100, ft2 * 100, pieChartDataModel)
                Column(horizontalAlignment=Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(painterResource(R.drawable.ic_icon_num), null)
                        Text(
                            modifier = Modifier.padding(bottom = 7.dp),
                            text = "" + ft1 * 100+"%",
                            fontSize = 24.sp,
                            color = Color(0xFF3C8EED)
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top=5.dp),
                        text = data.title+"成功率",
                        fontSize = 20.sp,
                        color = Color(0xFF222222)
                    )
                }

            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .background(Color(0xFFF5F7FA))
                    .padding(20.dp),
                text = data.chart.subtitle,
                fontSize = 20.sp,
                color = Color(0xFF222222)
            )

            Surface(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .height(1.dp), color = Color.Gray
            ) {}

        }
    }
}


@Composable
fun defBox(){
    Column(Modifier.padding(top = 40.dp)) {
        Image(painterResource( R.drawable.ic_icon_rep2 ), null)
        Row(Modifier.padding(top = 12.dp),verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource( R.drawable.ic_icon_bq ), null)
            Text(
                modifier  =Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                text = "协议离婚解释",
                fontSize = 20.sp,
                color = Color(0xFF222222)
            )
        }

        Text(
            modifier =Modifier.padding(top = 20.dp),
            text = "协议离婚又称两愿离婚或登记离婚，我国《婚姻法》中称作双方自愿离婚，指婚姻关系因双方当事人的合意而解除的离婚方式。",
            fontSize = 20.sp,
            color = Color(0xFF666666)
        )
        Surface(
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(1.dp),color = Color.Gray) {}
    }

}

@Composable
fun stepBox(){
    Column(Modifier.padding(top = 40.dp,bottom = 20.dp)) {
        Image(painterResource(R.drawable.ic_icon_rep3), null)
        WebView("", stepdata.replace("\n", ""))

        Surface(
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(1.dp), color = Color.Gray
        ) {}
    }
}

@Composable
fun sugBox(){
    Column(Modifier.padding(top = 40.dp,bottom = 20.dp)) {
        Image(painterResource(R.drawable.ic_icon_rep4), null)
        WebView("", sugdata.replace("\n", ""))
    }
}

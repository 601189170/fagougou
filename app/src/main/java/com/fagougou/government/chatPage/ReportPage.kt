package com.fagougou.government.chatPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.fagougou.government.chatPage.ReportMainModel.content
import com.fagougou.government.chatPage.ReportMainModel.stepdata
import com.fagougou.government.chatPage.ReportMainModel.sugdata
import com.fagougou.government.chatPage.ReportMainModel.toplist
import com.fagougou.government.chatPage.ReportMainModel.type
import com.fagougou.government.model.AttachmentContent
import com.fagougou.government.view.PieChartDataModel
import com.fagougou.government.webViewPage.WebView
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer

@Composable
fun Report (navController: NavController){
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 100.dp)) {
        if (toplist.isNotEmpty()) TopListBox()
        if (type=="pie") content?.let { ChartBox(it) }
        if (!stepdata.isBlank()) StepBox()
        if (!sugdata.isBlank()) SugBox()
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
fun TopListBox() {
    Column(Modifier.height(130.dp)) {
            LazyHorizontalGrid( modifier = Modifier.height(129.dp),rows = GridCells.Fixed(1),contentPadding=PaddingValues(20.dp),userScrollEnabled = false,
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
                                    .padding(horizontal = 20.dp,vertical = 10.dp),
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
                .height(1.dp), color = Color.Gray) {}
    }
}

@Composable
fun ChartBox(data: AttachmentContent){
    if (data.chart.data.isNotEmpty()) {
        Column(Modifier.padding(top = 40.dp)) {
            Image(painterResource(R.drawable.ic_icon_rep1), null)
            Text(
                "根据案情，法院支持您"+data.chart.title+"为:",
                Modifier.padding(top = 12.dp),
                Color(0xFF222222),
                24.sp,
                fontWeight = FontWeight.Bold,
            )
            val ft1 = data.chart.data[0].value
            val ft2 = data.chart.data[1].value
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp), contentAlignment = Alignment.Center) {
                val pieChartDataModel = remember { PieChartDataModel() }
                PieChartRow(ft1 * 100, ft2 * 100, pieChartDataModel)
                Column(horizontalAlignment=Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(painterResource(R.drawable.ic_icon_num), null)
                        Text(
                            "" + ft1 * 100+"%",
                            Modifier.padding(bottom = 7.dp),
                            Color(0xFF3C8EED),
                            24.sp
                        )
                    }
                    Text(
                        data.title+"成功率",
                        Modifier.padding(top=5.dp),
                        Color(0xFF222222),
                        20.sp
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
                "协议离婚解释",
                Modifier.padding(start = 8.dp),
                Color(0xFF222222),
                20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            "协议离婚又称两愿离婚或登记离婚，我国《婚姻法》中称作双方自愿离婚，指婚姻关系因双方当事人的合意而解除的离婚方式。",
            Modifier.padding(top = 20.dp),
            Color(0xFF666666),
            20.sp,
        )
        Surface(
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(1.dp),color = Color.Gray
        ) {}
    }

}

@Composable
fun StepBox(){
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
fun SugBox(){
    Column(Modifier.padding(top = 40.dp,bottom = 20.dp)) {
        Image(painterResource(R.drawable.ic_icon_rep4), null)
        WebView("", sugdata.replace("\n", ""))
    }
}


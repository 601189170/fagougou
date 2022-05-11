package com.fagougou.xiaoben.chatPage

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.Header
import com.fagougou.xiaoben.chatPage.Case.data
import com.fagougou.xiaoben.model.CaseData
import com.fagougou.xiaoben.webViewPage.WebView

object Case{
    var data = CaseData()
}

@Composable
fun CasePage(navController: NavController) {
    Surface(color = Color.White) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Header(title = "相关案例", navController = navController)
            Text(modifier = Modifier.padding(top = 16.dp),text = data.案件名称,fontSize = 28.sp)
            Row(
                modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(data.法院名称,fontSize = 22.sp)
                Text(data.审判程序,fontSize = 22.sp)
                Text(data.裁判日期,fontSize = 22.sp)
            }
            WebView("", data.DocContent)
        }
    }

}
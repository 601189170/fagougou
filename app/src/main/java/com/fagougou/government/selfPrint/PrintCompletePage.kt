package com.fagougou.government.selfPrint

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.Header
import com.fagougou.government.ui.theme.Dodgerblue

@Composable
fun PrintCompletePage(subNavController:NavController,navController: NavController) {
    Column(Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(R.drawable.img_phone_step2), null ,Modifier.padding(top = 72.dp))
        Text(
            "打印成功",
            Modifier.padding(top = 24.dp),
            fontSize = 28.sp,
            color = Color(0xFF303133)
        )
        Text(
            "文件打印成功，请在下方打印机口取走相关文件，",
            Modifier.padding(top = 24.dp),
            fontSize = 28.sp,
            color = Color(0xFF303133)
        )
        Row(
            Modifier.padding(top = 32.dp).fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterVertically
        ){
            Button(
                { subNavController.popBackStack(Router.SelfPrint.guide,false) },
                Modifier.height(64.dp).width(200.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Dodgerblue)
            ){
                Text("继续打印",Modifier,Color.White,24.sp)
            }
            Spacer(Modifier.width(24.dp))
            Button(
                { navController.popBackStack() },
                Modifier.height(64.dp).width(200.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(2.dp, Dodgerblue),
                contentPadding = PaddingValues(horizontal = 36.dp,vertical = 12.dp),
                elevation = ButtonDefaults.elevation(0.dp,0.dp),
            ){ Text("返回首页",Modifier,Dodgerblue,24.sp) }
        }
    }
}
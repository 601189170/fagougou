package com.fagougou.government.setting


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
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
import com.fagougou.government.component.Header
import com.fagougou.government.utils.ZYSJ.hideBar
import com.fagougou.government.utils.ZYSJ.reboot
import com.fagougou.government.utils.ZYSJ.showBar
import com.fagougou.government.utils.ZYSJ.shutdown

@Composable
fun Settings(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "设置", navController = navController)
        Row(
            modifier = Modifier
                .padding(vertical = 32.dp).padding(end = 32.dp)
                .fillMaxWidth()
                .clickable { hideBar() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(start = 32.dp),
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp,
                text = "隐藏导航栏"
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            modifier = Modifier
                .padding(vertical = 32.dp).padding(end = 32.dp)
                .fillMaxWidth()
                .clickable { showBar() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .clickable { showBar() },
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp,
                text = "打开导航栏"
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            modifier = Modifier
                .padding(vertical = 32.dp).padding(end = 32.dp)
                .fillMaxWidth()
                .clickable { showBar() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .clickable { reboot() },
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp,
                text = "重启"
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            modifier = Modifier
                .padding(vertical = 32.dp).padding(end = 32.dp)
                .fillMaxWidth()
                .clickable { showBar() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .clickable { shutdown() },
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp,
                text = "关机"
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
    }
}
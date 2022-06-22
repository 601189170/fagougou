package com.fagougou.government.setting

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.R
import com.fagougou.government.component.Header
import com.fagougou.government.utils.MMKV
import com.fagougou.government.utils.TTS
import com.fagougou.government.utils.ZYSJ.hideBar
import com.fagougou.government.utils.ZYSJ.reboot
import com.fagougou.government.utils.ZYSJ.showBar
import com.fagougou.government.utils.ZYSJ.shutdown
import com.iflytek.cloud.SpeechConstant

@Composable
fun Settings(navController: NavController) {
    Column(Modifier.fillMaxSize()) {
        Header("设置", navController)
        Row(
            Modifier
                .padding(vertical = 32.dp)
                .padding(end = 32.dp)
                .fillMaxWidth()
                .clickable { hideBar() },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Text(
                "隐藏导航栏",
                Modifier.padding(start = 32.dp),
                Color.White,
                fontSize = 24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp,
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            Modifier
                .padding(vertical = 32.dp)
                .padding(end = 32.dp)
                .fillMaxWidth()
                .clickable { showBar() },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Text(
                "打开导航栏",
                Modifier.padding(start = 32.dp).clickable { showBar() },
                Color.White,
                24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            Modifier
                .padding(vertical = 32.dp)
                .padding(end = 32.dp)
                .fillMaxWidth()
                .clickable { reboot() },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Text(
                "重启",
                Modifier
                    .padding(start = 32.dp),
                Color.White,
                fontSize = 24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            Modifier
                .padding(vertical = 32.dp)
                .padding(end = 32.dp)
                .fillMaxWidth()
                .clickable { shutdown() },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Text(
                "关机",
                Modifier.padding(start = 32.dp),
                Color.White,
                24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            Modifier
                .padding(vertical = 32.dp)
                .padding(end = 32.dp)
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS)
                    CommonApplication.activity.startActivity(intent)
                },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Text(
                "设置",
                Modifier.padding(start = 32.dp),
                Color.White,
                24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp
            )
            Image(painterResource(R.drawable.ic_right),null)
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            Modifier
                .padding(vertical = 32.dp)
                .padding(end = 32.dp)
                .fillMaxWidth()
                .clickable {

                },
            Arrangement.Start,
            Alignment.CenterVertically
        ) {
            Text(
                "语速设置",
                Modifier.padding(start = 32.dp),
                Color.White,
                24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp
            )
            val progress  = remember {
               val speed=TTS.mTts.getParameter(SpeechConstant.SPEED).toFloat()/100
               mutableStateOf(speed)
            }
            Slider(
                progress.value,
                {
                    progress.value = it
                    val speed=(it*100).toInt()
                    TTS.mTts.setParameter(SpeechConstant.SPEED, speed.toString())
                    MMKV.kv.encode(MMKV.robootSpeed,speed.toString())
                },
                Modifier.width(600.dp).padding(start = 32.dp),
                colors = SliderDefaults.colors(
                    inactiveTrackColor = Color.LightGray,
                    activeTrackColor = Color.Blue
                )
            )
            Text(
                TTS.mTts.getParameter(SpeechConstant.SPEED),
                Modifier.padding(start = 32.dp),
                Color.White,
                24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp
            )
        }
        Divider(
            Modifier.padding(top = 5.dp),
            color = Color(0xFFFFFFFF),
            thickness = 2.dp,
        )
        Row(
            Modifier
                .padding(vertical = 32.dp)
                .padding(end = 32.dp)
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS)
                    CommonApplication.activity.startActivity(intent)
                },
            Arrangement.SpaceBetween,
            Alignment.CenterVertically) {
            Text(
                "序列号:${CommonApplication.serial}",
                Modifier.padding(start = 32.dp),
                Color.White,
                24.sp,
                lineHeight = 35.sp,
                letterSpacing = 1.2f.sp
            )
        }

    }
}
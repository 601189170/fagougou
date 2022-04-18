package com.fagougou.xiaoben

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.fagougou.xiaoben.ui.theme.XiaoBenTheme
import com.fagougou.xiaoben.utils.IFly
import com.fagougou.xiaoben.utils.Tips.toast
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.iflytek.cloud.ErrorCode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XiaoBenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Home()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Home() {
    val pagerState = rememberPagerState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Row(
            modifier = Modifier.fillMaxHeight(0.133f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "丰台法治工商小助手", fontSize = 32.sp)
        }
        HorizontalPager(
            count = 2,
            state = pagerState,
            modifier = Modifier.fillMaxHeight(0.96f),
        ) { index ->
            when (index) {
                0 -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        content = { Text(IFly.recordingState.value) },
                        onClick = {
                            with(IFly){
                                if (recordingState.value!="开始录音")return@with
                                setParam()
                                val ret = mIat.startListening(mRecognizerListener)
                                if (ret != ErrorCode.SUCCESS) toast("听写失败,错误码：$ret")
                            }
                        }
                    )
                    Text(IFly.result.value)
                }
                1 -> Text(text = "第二页")
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalPagerIndicator( pagerState = pagerState )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    XiaoBenTheme {
        Home()
    }
}
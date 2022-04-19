package com.fagougou.xiaoben

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import com.fagougou.xiaoben.utils.IFly

@Composable
fun RecordingPage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(IFly.wakeUpResult.value, fontSize = 32.sp)
        Text(IFly.recognizeResult.value, fontSize = 32.sp)
    }
}
package com.fagougou.xiaoben

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.fagougou.xiaoben.utils.IFly

@Composable
fun RecordingPage() {
    val scrollState = rememberScrollState(0)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        Text(IFly.wakeUpResult.value, fontSize = 32.sp)
        Text(IFly.recognizeResult.value, fontSize = 32.sp)
        Text(IFly.volumeState.value, fontSize = 32.sp)
        for (text in IFly.history) Text(text, fontSize = 32.sp)
        Button(
            content = { Text("清空", fontSize = 32.sp) },
            onClick = { IFly.history.clear() }
        )
    }
}
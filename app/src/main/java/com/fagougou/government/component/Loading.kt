package com.fagougou.government.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fagougou.government.Router
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.pop
import com.fagougou.government.ui.theme.CORNER_FLOAT
import timber.log.Timber

@Composable
fun Loading() {
    Timber.d("GlobalLoading:${Client.globalLoading.value}")
    if (Router.routeMirror in Router.noLoadingPages) return
    if (Client.globalLoading.value > 0) Surface(color = Color.Transparent) {
        Column(
            Modifier.fillMaxSize().clickable { Client.globalLoading.pop() },
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Surface(
                Modifier
                    .width(256.dp)
                    .height(256.dp),
                RoundedCornerShape(CORNER_FLOAT),
                Color(0x33000000)
            ) {
                CircularProgressIndicator(Modifier.padding(48.dp))

            }
        }
    }
}
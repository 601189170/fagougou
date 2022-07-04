package com.fagougou.government.lawyer

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.CommonApplication.Companion.activity
import com.fagougou.government.Router
import com.fagougou.government.chatPage.Case
import com.fagougou.government.chatPage.ReportMainModel
import com.fagougou.government.component.BasicText
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.model.CaseResponse
import com.fagougou.government.model.uploadBean
import com.fagougou.government.repo.Client
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*
import timber.log.Timber
object LawyersPageModel{


}

@Composable
fun LawyersPage(navController: NavController) {

    val uploadBitmap = remember{ mutableStateOf( QrCodeViewModel.bitmap("null") ) }
    LaunchedEffect( null ){
        val url = "https://a.b/selfPrint?taskId="+Time.stamp+"_"+(0..999999).random()
        uploadBitmap.value=QrCodeViewModel.bitmap(url)
    }

    Column(
        Modifier.fillMaxSize(),
        Arrangement.Top,
        Alignment.CenterHorizontally
    ) {
        Header("律师团队", navController,{QrCodeViewModel.clear()} )
        LazyHorizontalGrid( modifier = Modifier.fillMaxSize(),rows = GridCells.Fixed(6),contentPadding=PaddingValues(20.dp),userScrollEnabled = false,
            content = {
                items(18) { bot ->
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
                            text = "律师团队",
                            fontSize = 20.sp,
                            color = Color(0xFF0E7AE6)
                        )
                    }
                }
            })
    }
}
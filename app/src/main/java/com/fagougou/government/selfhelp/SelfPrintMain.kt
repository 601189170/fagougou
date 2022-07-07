package com.fagougou.government.selfhelp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.government.Router
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.contractReviewPage.*
import com.fagougou.government.model.StepModel


@Composable
fun SelfPrintMain(navController: NavController) {
    val navController2 = rememberNavController()
    val stepModel = remember{ StepModel(mutableStateListOf("文件上传","文档预览","完成打印"),mutableStateOf(0)) }
    val fullScreenMode = remember{ mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Top,
        Alignment.CenterHorizontally
    ) {
        Header("自助打印", navController,{QrCodeViewModel.clear()} )
        SelfHelpBase(stepModel,fullScreenMode){
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xF3F3F3F3)),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                NavHost(navController2, Router.self, Modifier.fillMaxHeight()) {
                    composable(Router.self) {
                        SelfPrintPage(navController2)
                        stepModel.currentIndex.value=0
                    }
                    composable(Router.uploading) {
                        Uploading(navController2)
                        stepModel.currentIndex.value=0
                    }
                    composable(Router.previewLoad) {
                        PreviewLoad(navController2,navController, fullScreenMode,Router.printComplete)
                        stepModel.currentIndex.value=1
                    }
                    composable(Router.printComplete) {
                        PrintCompletePage(navController)
                        stepModel.currentIndex.value=2
                    }
                }
            }
        }
    }
}
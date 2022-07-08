package com.fagougou.government.selfPrint

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.government.Router
import com.fagougou.government.component.Header
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.contractReviewPage.PreviewLoad
import com.fagougou.government.contractReviewPage.Uploading
import com.fagougou.government.model.StepModel

@Composable
fun SelfPrintMain(navController: NavController) {
    val navController2 = rememberNavController()
    val stepModel = remember{ StepModel(mutableStateListOf("文件上传","文档预览","完成打印"),mutableStateOf(0)) }
    val fullScreenMode = remember{ mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize(),
    ) {
        if(!fullScreenMode.value)Header("自助打印", navController, {QrCodeViewModel.clear()} )
        SelfHelpBase(stepModel,fullScreenMode){
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
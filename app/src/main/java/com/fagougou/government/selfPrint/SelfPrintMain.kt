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
import com.fagougou.government.component.uploadGroup.PreviewLoad
import com.fagougou.government.component.uploadGroup.Uploading
import com.fagougou.government.model.StepModel

@Composable
fun SelfPrintMain(navController: NavController) {
    val subNavController = rememberNavController()
    val stepModel = remember{ StepModel(mutableStateListOf("文件上传","文档预览","完成打印"),mutableStateOf(0)) }
    val fullScreenMode = remember{ mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize(),
    ) {
        if(!fullScreenMode.value)Header("自助打印", navController, {QrCodeViewModel.clear()} )
        SelfHelpBase(stepModel,fullScreenMode){
            NavHost(subNavController, Router.SelfPrint.guide, Modifier.fillMaxHeight()) {
                composable(Router.SelfPrint.guide) {
                    SelfPrintPage(subNavController)
                    stepModel.currentIndex.value=0
                }
                composable(Router.Upload.waiting) {
                    Uploading(subNavController)
                    stepModel.currentIndex.value=0
                }
                composable(Router.Upload.pdfPreview) {
                    PreviewLoad(subNavController, fullScreenMode,Router.SelfPrint.printComplete)
                    stepModel.currentIndex.value=1
                }
                composable(Router.SelfPrint.printComplete) {
                    PrintCompletePage(subNavController,navController)
                    stepModel.currentIndex.value=3
                }
            }
        }
    }
}
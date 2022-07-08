package com.fagougou.government.contractReviewPage

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
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.model.StepModel

@Composable
fun ContractSelectMain(navController: NavController) {
    val navController2 = rememberNavController()
    val stepModel = remember{ StepModel(mutableStateListOf("选择类型","文件上传","文档预览","完成审查"), mutableStateOf(0)) }
    val fullScreenMode = remember{ mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize()
    ) {
        if(!fullScreenMode.value)Header("智能合同审核", navController )
        SelfHelpBase(stepModel,fullScreenMode){
            NavHost(navController2, Router.contractSelectPage, Modifier.fillMaxHeight()) {
                composable(Router.contractSelectPage) {
                    ContractSelectPage(navController2)
                    stepModel.currentIndex.value=0
                }
                composable(Router.upload) {
                    UploadGuidePage(navController2)
                    stepModel.currentIndex.value=1
                }
                composable(Router.uploading) {
                    Uploading(navController2)
                    stepModel.currentIndex.value=1
                }
                composable(Router.previewLoad) {
                    PreviewLoad(navController2,navController,fullScreenMode, Router.resultWebview)
                    stepModel.currentIndex.value=2
                }
                composable(Router.resultWebview) {
                    ResultWebviewPage(navController)
                    stepModel.currentIndex.value=3
                }
            }
        }
    }
}
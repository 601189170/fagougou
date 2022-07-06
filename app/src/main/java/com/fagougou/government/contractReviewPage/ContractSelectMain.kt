package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.government.Router
import com.fagougou.government.component.Header
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.model.StepModel
import com.fagougou.government.repo.Client

@Composable
fun ContractSelectMain(navController: NavController) {
    val navController2 = rememberNavController()

    Column(
        Modifier.fillMaxSize(),
    ) {
        Header("智能合同审核", navController )
        val stepModel = remember{ StepModel(
            mutableStateListOf("选择类型","文件上传","文档预览","完成打印"),
            mutableStateOf(0)
        ) }

        SelfHelpBase(stepModel){
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xF3F3F3F3)),
                Arrangement.Top,
                Alignment.CenterHorizontally
            ) {
                NavHost(navController2, Router.contractSelectPage, Modifier.fillMaxHeight()) {
                    composable(Router.contractSelectPage) { ContractSelectPage(navController2)
                        stepModel.currentIndex.value=0}
                    composable(Router.upload) { UploadGuidePage(navController2)
                        stepModel.currentIndex.value=1}
                    composable(Router.uploading) { Uploading(navController2,"")
                        stepModel.currentIndex.value=1}
                    composable(Router.previewLoad) { Previewload(navController2,navController, "")
                        stepModel.currentIndex.value=2}
                    composable(Router.resultWebview) { ResultWebviewPage(navController)
                        stepModel.currentIndex.value=3}
                }
            }
        }
    }
}
package com.fagougou.government.contractReviewPage

import android.content.Intent
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
import com.fagougou.government.CommonApplication
import com.fagougou.government.Router
import com.fagougou.government.chatPage.ChatViewModel
import com.fagougou.government.component.Header
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.component.uploadGroup.PreviewLoad
import com.fagougou.government.component.uploadGroup.UploadGuidePage
import com.fagougou.government.component.uploadGroup.Uploading
import com.fagougou.government.consult.TouristsLoginActivity
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.model.ContentStyle
import com.fagougou.government.model.StepModel
import com.fagougou.government.utils.IFly
import com.fagougou.government.utils.SafeBack.safeBack

@Composable
fun ContractSelectMain(navController: NavController) {
    val subNavController = rememberNavController()
    val stepModel = remember{ StepModel(mutableStateListOf("选择类型","文件上传","文档预览","完成审查"), mutableStateOf(0)) }
    val fullScreenMode = remember{ mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize()
    ) {
        if(!fullScreenMode.value)Header("智能合同审查", navController,
            {
                with(DialogViewModel){
                    clear()
                    title = "温馨提示"
                    canExit = true
                    firstButtonText.value = "取消"
                    firstButtonOnClick.value = {
                        clear()
                    }
                    secondButtonText.value = "确定"
                    secondButtonOnClick.value = {
                        clear()
                        navController.safeBack()
                    }
                    content.add(ContentStyle("退出页面后将丢失本次审查信息"))
                }
            },false,isCases = true)
        SelfHelpBase(stepModel,fullScreenMode){
            NavHost(subNavController, Router.ContractReview.classify, Modifier.fillMaxHeight()) {
                composable(Router.ContractReview.classify) {
                    ContractSelectPage(subNavController)
                    stepModel.currentIndex.value=0
                }
                composable(Router.ContractReview.guide) {
                    UploadGuidePage(subNavController,navController)
                    stepModel.currentIndex.value=1
                }
                composable(Router.ContractReview.camera) {
                    UploadGuidePage(subNavController,navController)
                    stepModel.currentIndex.value=1
                }
                composable(Router.Upload.waiting) {
                    Uploading(subNavController)
                    stepModel.currentIndex.value=1
                }
                composable(Router.Upload.pdfPreview) {
                    PreviewLoad(subNavController,fullScreenMode, Router.ContractReview.result)
                    stepModel.currentIndex.value=2
                }
//                composable(Router.ContractReview.result) {
//                    ResultWebviewPage(navController)
//                    stepModel.currentIndex.value=3
//                }
            }
        }
    }
}
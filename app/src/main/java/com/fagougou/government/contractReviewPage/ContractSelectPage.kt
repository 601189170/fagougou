package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.fagougou.government.component.Header
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.model.StepModel

@Composable
fun ContractSelectPage(navController: NavController) {
    val stepModel = remember{
        StepModel(
            mutableStateListOf("选择类型","文件上传","文档预览","查看报告"),
            mutableStateOf(0)
        )
    }
    Column(
        Modifier.fillMaxSize(),
    ) {
        Header("智能合同审核", navController )
        SelfHelpBase(stepModel) {}
    }
}
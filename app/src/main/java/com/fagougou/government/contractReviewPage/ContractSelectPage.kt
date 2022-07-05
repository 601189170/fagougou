package com.fagougou.government.contractReviewPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.fagougou.government.component.ExBackground
import com.fagougou.government.component.ExContent
import com.fagougou.government.component.Header

@Composable
fun ContractSelectPage(navController: NavController) {
    Column(
        Modifier.fillMaxSize(),
    ) {
        Header("智能合同审核", navController )
        ExBackground()
        ExContent()
    }
}
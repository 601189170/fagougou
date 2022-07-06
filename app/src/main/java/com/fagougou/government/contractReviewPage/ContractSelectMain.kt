

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fagougou.government.Router
import com.fagougou.government.Router.contractSelectPage
import com.fagougou.government.chatPage.CasesPage
import com.fagougou.government.chatPage.LawsPage
import com.fagougou.government.chatPage.Report

import com.fagougou.government.component.Header
import com.fagougou.government.component.SelfHelpBase
import com.fagougou.government.component.VerticalGrid
import com.fagougou.government.contractReviewPage.ContractSelectPage
import com.fagougou.government.contractReviewPage.ResultWebviewPage
import com.fagougou.government.contractReviewPage.Scanupload
import com.fagougou.government.contractReviewPage.UploadPage
import com.fagougou.government.homePage.HomeButton
import com.fagougou.government.model.StepModel
import com.fagougou.government.webViewPage.WebViewPageModel

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
                    .fillMaxWidth()
                    .background(Color(0xF3F3F3F3)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column( Modifier.fillMaxSize(), Arrangement.Top, Alignment.CenterHorizontally ) {
                    NavHost(navController2, Router.contractSelectPage, Modifier.fillMaxHeight()) {
                        composable(Router.contractSelectPage) { ContractSelectPage(navController2) }
                        composable(Router.upload) { UploadPage(navController2) }
                        composable(Router.scanUpload) { Scanupload(navController2) }
                        composable(Router.resultWebview) { ResultWebviewPage(navController2) }
                    }
                }
            }
        }



    }
}
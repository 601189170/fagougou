package com.fagougou.xiaoben.contractPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.xiaoben.Headder
import com.fagougou.xiaoben.R
import com.fagougou.xiaoben.contractPage.Contract.categoryList
import com.fagougou.xiaoben.model.ContractCategory
import com.fagougou.xiaoben.model.ContractCategoryResponse
import com.fagougou.xiaoben.repo.Client.contractService
import com.fagougou.xiaoben.webViewPage.WebViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder

object Contract{
    val categoryList = mutableStateListOf<ContractCategory>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val response = contractService.listCategory().execute()
            val body = response.body() ?: ContractCategoryResponse()
            categoryList.addAll(body.categorys)
        }
    }
}

@Composable
fun ContractGuidePage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Headder(title = "合同文库", navController = navController)
        Button(
            onClick = {
                val rawUrl = "https://contract-template-1254426977.cos.ap-guangzhou.myqcloud.com/docx/i64g9JuABuFT7-RYY0MlX.docx?q-sign-algorithm=sha1&q-ak=AKIDY0eYMzOkqlolZDS1BAya2MYYN1Unr62r&q-sign-time=1651028204;1651028504&q-key-time=1651028204;1651028504&q-header-list=&q-url-param-list=&q-signature=25dd6ea837dfb54358a18dc13826dba1f50f4f30"
                val encodedUrl = URLEncoder.encode(rawUrl,"UTF-8")
                WebViewModel.WebViewUrl = "https://view.officeapps.live.com/op/view.aspx?src=$encodedUrl"
                navController.navigate("WebView")
                      },
            content = {
                Text("快速访问")
            }
        )
        Surface(
            modifier = Modifier
                .height(392.dp)
                .width(1080.dp)
        ) {
            Image(painterResource(id = R.drawable.contract_banner),null)
            Column(
                modifier = Modifier
                    .height(392.dp)
                    .width(1080.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "海量合同文库", fontSize = 72.sp, color = Color.White,fontWeight = FontWeight.Bold)
            }
        }
        Row(Modifier.fillMaxSize()) {
            Surface(color = Color(0xFFEEEEEE)) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxSize(0.4f),
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(48.dp),
                        content = {
                            items(categoryList){ category ->
                                Text(category.name, fontSize = 32.sp)
                            }
                        }
                    )
                }
            }
            Surface(color = Color(0xFFFFFFFF)) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxSize()) {

                }
            }
        }
    }

}
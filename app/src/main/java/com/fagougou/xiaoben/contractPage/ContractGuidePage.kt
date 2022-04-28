package com.fagougou.xiaoben.contractPage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import com.fagougou.xiaoben.contractPage.Contract.HTLists
import com.fagougou.xiaoben.contractPage.Contract.categoryList
import com.fagougou.xiaoben.contractPage.Contract.currentSelect
import com.fagougou.xiaoben.contractPage.Contract.getHTLIST
import com.fagougou.xiaoben.contractPage.Contract.getTemplate
import com.fagougou.xiaoben.contractPage.ContractWebView.codeUrl
import com.fagougou.xiaoben.model.ContractCategory
import com.fagougou.xiaoben.model.DataB
import com.fagougou.xiaoben.model.HTListRequest
import com.fagougou.xiaoben.repo.Client.contractService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder


object Contract{
    val categoryList = mutableStateListOf<ContractCategory>()
    val HTLists = mutableStateListOf<DataB>()
    var currentSelect = mutableStateOf(0)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val response = contractService.listCategory().execute()
            val body = response.body()
            if (body!=null) categoryList.addAll(body.categorys)
            getHTLIST(categoryList.firstOrNull()?.id ?: return@launch)
        }
    }

    fun  getHTLIST(folder:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = contractService.getHtlist(HTListRequest(folder = folder)).execute()
            val body = response.body() ?: return@launch
            HTLists.clear()
            HTLists.addAll(body.data.list)
        }
    }

    fun  getTemplate(fileid:String,navController: NavController) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = contractService.template(fileid).execute()
            val body = response.body() ?: return@launch
            withContext(Dispatchers.Main){
                codeUrl = body.data
                val encodedUrl = URLEncoder.encode(codeUrl,"UTF-8")
                ContractWebView.webViewUrl = "https://view.officeapps.live.com/op/view.aspx?src=$encodedUrl"
                navController.navigate("contractWebView")
            }
        }
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun ContractGuidePage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Headder(title = "合同文库", navController = navController)
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
                        modifier = Modifier.padding(vertical = 36.dp),
                        content = {
                            items(categoryList.size){ index ->
                                Surface(
                                    color = if(currentSelect.value==index)Color(0xFFBBCCEE) else Color(0xFFEEEEEE)
                                ) {
                                    Button(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                                        elevation = ButtonDefaults.elevation(0.dp),
                                        content = {
                                            Text(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                text = categoryList[index].name,
                                                fontSize = 28.sp,
                                                color = Color.Black
                                            )
                                        },
                                        onClick = {
                                            currentSelect.value = index
                                            getHTLIST(categoryList[index].id)
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            }
            Surface(color = Color(0xFFFFFFFF)) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(48.dp),
                        content = {
                            items(HTLists){ category ->
                                Button(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    elevation = ButtonDefaults.elevation(0.dp),
                                    content = {
                                          Column{
                                              Row(verticalAlignment = Alignment.CenterVertically) {
                                                  Image(painterResource(R.drawable.icon_hti_head), null)
                                                  Text(
                                                      modifier = Modifier.padding(start = 8.dp),
                                                      fontWeight= FontWeight.Bold,
                                                      text = category.name,
                                                      fontSize = 28.sp,
                                                      color = Color.Black
                                                  )
                                              }
                                              Text(
                                                  modifier = Modifier.padding(top = 12.dp),
                                                  text = category.howToUse.replace("\n",""),
                                                  fontSize = 24.sp,
                                                  color = Color.Black
                                              )
                                              Row(
                                                  modifier = Modifier.padding(top = 12.dp),
                                              ) {
                                                  Text(
                                                      maxLines=1,
                                                      text = "行业类型："+ category.folder.name,
                                                      fontSize = 20.sp,
                                                      color = Color.Gray
                                                  )
                                                  Text(
                                                      maxLines=1,
                                                      modifier = Modifier.padding(start = 8.dp),
                                                      text = "更新时间："+ category.updatedAt,
                                                      fontSize = 20.sp,
                                                      color = Color.Gray
                                                  )
                                              }
                                          }
                                    },
                                    onClick = { getTemplate(category.fileid, navController) }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

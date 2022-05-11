package com.fagougou.xiaoben.generateContract

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fagougou.xiaoben.Header
import com.fagougou.xiaoben.generateContract.GenerateContract.content
import com.fagougou.xiaoben.generateContract.GenerateContract.contractList
import com.fagougou.xiaoben.generateContract.GenerateContract.getGenerateTemplete
import com.fagougou.xiaoben.generateContract.GenerateContract.notifier
import com.fagougou.xiaoben.model.*
import com.fagougou.xiaoben.repo.Client.generateService
import com.fagougou.xiaoben.webViewPage.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object GenerateContract {
    val contractList = mutableStateListOf<GenerateContractBrief>()
    val content = mutableStateOf("")
    val formList = mutableStateListOf(GenerateForm())
    val notifier = mutableStateOf("")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val response = generateService.getGeneratelist(GenerateListRequest()).execute()
            val body = response.body()?.data ?: GenerateContractListResponse().data
            contractList.addAll(body.list)
        }
    }

    suspend fun getGenerateTemplete(id:String){
        withContext(Dispatchers.IO){
            formList.clear()
            val response = generateService.getGenrateForm(id).execute()
            val body = response.body()?.data ?: GenerateData()
            formList.addAll(body.forms)
        }
    }
}

@Composable
fun GenerateContract(navController: NavController) {
    val scope = rememberCoroutineScope()
    Surface(color = Color.White) {
        Text(notifier.value)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            Header(title = "合同生成", navController = navController)
            Row(modifier = Modifier.fillMaxWidth()) {
                for (item in contractList) {
                    Button(
                        onClick = {
                            scope.launch {
                                getGenerateTemplete(item.id)
                            }
                        },
                        content = { Text(item.name) }
                    )
                }
            }
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(0.6f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    for (item in GenerateContract.formList) {
                        Text(item.label)
                        for (child in item.children) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.width(256.dp),
                                    text = child.label
                                )
                                TextField(
                                    modifier = Modifier.width(512.dp),
                                    value = child.value,
                                    onValueChange = { str ->
                                        child.value = str
                                        content.value = str
                                        notifier.value = str
                                    },
                                    placeholder = {
                                        if (child.value == "")Text(child.comment)
                                    }
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    WebView(content)
                }
            }
        }
    }
}
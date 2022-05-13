package com.fagougou.government.generateContract

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.Header
import com.fagougou.government.Router
import com.fagougou.government.generateContract.GenerateContract.content
import com.fagougou.government.generateContract.GenerateContract.contractList
import com.fagougou.government.generateContract.GenerateContract.getGenerateForm
import com.fagougou.government.generateContract.GenerateContract.getGenerateTemplete
import com.fagougou.government.generateContract.GenerateContract.lastModifier
import com.fagougou.government.generateContract.GenerateContract.notifier
import com.fagougou.government.model.*
import com.fagougou.government.repo.Client.generateService
import com.fagougou.government.utils.Handlebars
import com.fagougou.government.utils.IFly.routeMirror
import com.fagougou.government.utils.Time
import com.fagougou.government.webViewPage.WebView
import kotlinx.coroutines.*

object GenerateContract {
    val contractList = mutableStateListOf<GenerateContractBrief>()
    val content = mutableStateOf("")
    var template = ""
    val formList = mutableStateListOf(GenerateForm())
    val notifier = mutableStateOf("")
    var lastModifier = Time.stamp

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val response = generateService.getGeneratelist(GenerateListRequest()).execute()
            val body = response.body()?.data ?: GenerateContractListResponse().data
            contractList.addAll(body.list)
        }
        CoroutineScope(Dispatchers.Default).launch {
            while(true){
                delay(750)
                if(routeMirror == Router.generateContract)updateContent()
            }
        }
    }

    fun clear(){
        content.value = ""
        template = ""
        formList.clear()
        lastModifier = Time.stamp
    }

    suspend fun getGenerateForm(id:String){
        withContext(Dispatchers.IO){
            formList.clear()
            val response = generateService.getGenrateForm(id).execute()
            val body = response.body()?.data ?: GenerateData()
            formList.addAll(body.forms)
        }
    }

    suspend fun getGenerateTemplete(id:String){
        withContext(Dispatchers.IO){
            formList.clear()
            val response = generateService.getGenrateTemplete(id).execute()
            val body = response.body()?.data ?: GenerateContractTemplete()
            template = body.content
        }
    }

    suspend fun updateContent(){
        withContext(Dispatchers.Default){
            val builder = StringBuilder()
            for(item in formList) {
                for (child in item.children) {
                    with(child){
                        val result = when(type){
                            "checkbox"->{
                                if(selected.isNotEmpty()){
                                    val builder = StringBuilder()
                                    for(select in selected){
                                        builder.append(values[select])
                                    }
                                    val result = builder.toString()
                                    if (result.isNotEmpty())result.substring(0,builder.lastIndex)
                                    else ""
                                }
                                else ""
                            }
                            "select"->{
                                if(selected.isNotEmpty()) values[selected.first()] else ""
                            }
                            else -> if(input!="") input else "__________"
                        }
                        builder.append("${variable}:\"$result\",")
                    }
                }
            }
            val result = Handlebars.templete
                .replace("{{TemplateHook}}", template)
                .replace("{{DataHook}}",builder.toString())
                .replace("class=\"$lastModifier","style=\"background-color: yellow;\" class=\"$lastModifier")
            content.value = result
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
            Header(
                title = "合同生成",
                navController = navController,
                onBack = { GenerateContract.clear() }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                for (item in contractList) {
                    Button(
                        onClick = {
                            GenerateContract.clear()
                            scope.launch {
                                getGenerateTemplete(item.id)
                            }
                            scope.launch {
                                getGenerateForm(item.id)
                            }
                        },
                        content = { Text(item.name) }
                    )
                }
            }
            Row(modifier = Modifier.fillMaxSize()) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(0.6f)
                        .verticalScroll(scrollState),
                ) {
                    for (item in GenerateContract.formList) {
                        Text(item.label,fontSize = 24.sp)
                        for (child in item.children) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.width(256.dp),
                                    text = child.label
                                )
                                when(child.type){
                                    "checkbox" -> {
                                        Column {
                                            for((i,option) in child.values.withIndex()){
                                                Row {
                                                    Checkbox(
                                                        checked = i in child.selected,
                                                        onCheckedChange = {
                                                            if(it) child.selected.add(i)
                                                            else child.selected.remove(i)
                                                            lastModifier = child.variable
                                                            notifier.value = Time.stamp
                                                        }
                                                    )
                                                    Text(option)
                                                }
                                            }
                                        }
                                    }
                                    "select" -> {
                                        Column {
                                            for((i,option) in child.values.withIndex()){
                                                Row {
                                                    RadioButton(
                                                        selected = i in child.selected,
                                                        onClick = {
                                                            child.selected.clear()
                                                            child.selected.add(i)
                                                            lastModifier = child.variable
                                                            notifier.value = Time.stamp
                                                        }
                                                    )
                                                    Text(option)
                                                }
                                            }
                                        }
                                    }
                                    else -> TextField(
                                        modifier = Modifier.width(512.dp),
                                        value = child.input,
                                        onValueChange = { str ->
                                            child.input = str
                                            lastModifier = child.variable
                                            notifier.value = Time.stamp
                                        },
                                        placeholder = {
                                            if (child.input == "")Text(child.comment)
                                        }
                                    )
                                }
                            }
                            Divider(thickness = 2.dp)
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
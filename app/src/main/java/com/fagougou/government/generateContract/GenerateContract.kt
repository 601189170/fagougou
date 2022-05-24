package com.fagougou.government.generateContract

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fagougou.government.CommonApplication
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.Header
import com.fagougou.government.dialog.DialogViewModel
import com.fagougou.government.generateContract.GenerateContract.data
import com.fagougou.government.generateContract.GenerateContract.lastModifier
import com.fagougou.government.generateContract.GenerateContract.notifier
import com.fagougou.government.model.*
import com.fagougou.government.repo.Client.generateService
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.utils.Time
import kotlinx.coroutines.*
import java.io.InputStreamReader

object GenerateContract {
    val contractList = mutableStateListOf<GenerateContractBrief>()
    val currentContractId = mutableStateOf("")
    var baseHtml = ""
    var template = ""
    val data = mutableStateOf("")
    val formList = mutableStateListOf(GenerateForm())
    val notifier = mutableStateOf("")
    var lastModifier = Time.stamp

    fun init(context: Context) {
        val file = context.assets.open("generateContract.html")
        baseHtml = InputStreamReader(file,"UTF-8").readText()
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = generateService.getGeneratelist(GenerateListRequest()).execute()
                val body = response.body()?.data ?: GenerateContractListResponse().data
                contractList.addAll(body.list)
            } catch (e:Exception) {
                handleException(e)
            }
        }
    }

    fun clear(){
        currentContractId.value = ""
        template = ""
        data.value = ""
        formList.clear()
        lastModifier = Time.stamp
    }

    suspend fun getGenerateForm(id:String){
        withContext(Dispatchers.IO){
            formList.clear()
            try{
                val response = generateService.getGenrateForm(id).execute()
                val body = response.body()?.data ?: GenerateData()
                formList.addAll(body.forms)
            }catch (e:Exception){
                handleException(e)
            }
        }
    }

    suspend fun getGenerateTemplate(id:String){
        withContext(Dispatchers.IO){
            template = ""
            try {
                val response = generateService.getGenrateTemplete(id).execute()
                val body = response.body()?.data ?: GenerateContractTemplete()
                template = body.content
            }catch (e:Exception){
                handleException(e)
            }
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
            val result = baseHtml
                .replace("{{TemplateHook}}", template)
                .replace("{{DataHook}}",builder.toString())
                .replace("class=\"$lastModifier","style=\"background-color: yellow;\" class=\"$lastModifier")
            data.value = result
        }
    }
}

@Composable
fun ContractWebView(data: MutableState<String>){
    AndroidView(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.88f),
        factory = {
            WebView(CommonApplication.activity).apply {
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                setInitialScale(80)
                settings.javaScriptEnabled = true
                isVerticalScrollBarEnabled = false
                isClickable = false
                isLongClickable = false
                webChromeClient = WebChromeClient()
            }
        },
        update = {
            it.loadDataWithBaseURL(null, data.value, "text/html; charset=utf-8", "utf-8", null)
            CoroutineScope(Dispatchers.Default).launch {
                delay(100)
                withContext(Dispatchers.Main){
                    it.evaluateJavascript("javascript:getHtml()",{ Log.i("html",it)})
                }
            }
        }
    )
}

@Composable
fun GenerateContract(navController: NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        while(isActive){
            delay(750)
            GenerateContract.updateContent()
        }
    }
    Surface(color = Color.White) {
        Text(notifier.value)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            Header(
                title = "智能文书",
                navController = navController,
                onBack = { GenerateContract.clear() }
            )
            Row(modifier = Modifier.fillMaxSize()) {
                val scrollState = rememberScrollState()
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.6f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        Modifier.fillMaxHeight(0.88f)
                    ){
                        ContractWebView(data)
                    }
                    Divider(thickness = 2.dp)
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){
//                        Button(
//                            modifier = Modifier
//                                .height(60.dp)
//                                .width(200.dp),
//                            onClick = { },
//                            content = {
//                                  Row( verticalAlignment = Alignment.CenterVertically ){
//                                      Image(painterResource(R.drawable.ic_wechat),null)
//                                      Text(
//                                          modifier = Modifier.padding(start = 16.dp),
//                                          text = "微信查看",
//                                          color = Color.White,
//                                          fontSize = 21.sp)
//                                  }
//                            },
//                            colors = buttonColors(backgroundColor = Dodgerblue),
//                            shape = RoundedCornerShape(18)
//                        )
                        Button(
                            modifier = Modifier
                                .height(60.dp)
                                .width(200.dp),
                            elevation = ButtonDefaults.elevation(0.dp,0.dp),
                            onClick = { DialogViewModel.startPrint(scope) },
                            content = {
                                Row( verticalAlignment = Alignment.CenterVertically ){
                                    Image(painterResource(R.drawable.ic_painter),null)
                                    Text(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = "打印合同",
                                        color = Color.White,
                                        fontSize = 21.sp)
                                }
                            },
                            colors = buttonColors(backgroundColor = Dodgerblue),
                            shape = RoundedCornerShape(16)
                        )
                    }
                }
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                ) {
                    for (item in GenerateContract.formList) {
                        Text(
                            modifier = Modifier.padding(top = 24.dp,start = 16.dp),
                            text = item.label,
                            fontSize = 24.sp
                        )
                        for (child in item.children) {
                            Column(Modifier.padding(horizontal = 16.dp)) {
                                Row(
                                    Modifier.padding(top = 24.dp,bottom = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Surface(
                                        Modifier
                                            .height(24.dp)
                                            .width(4.dp),color = Dodgerblue) { }
                                    Text(
                                        modifier = Modifier.padding(start = 12.dp),
                                        text = child.label,
                                        fontSize = 18.sp
                                    )
                                }
                                when(child.type){
                                    "checkbox" -> {
                                        Column {
                                            for((i,option) in child.values.withIndex()){
                                                Row(verticalAlignment = Alignment.CenterVertically) {
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
                                                Row(verticalAlignment = Alignment.CenterVertically) {
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
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(if(child.label.contains("地址"))112.dp else 56.dp)
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(18)),
                                        colors = TextFieldDefaults.textFieldColors(
                                            backgroundColor = Color.White,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                        ),
                                        value = child.input,
                                        onValueChange = { str ->
                                            Router.lastTouchTime = Time.stampL
                                            child.input = str
                                            lastModifier = child.variable
                                            notifier.value = Time.stamp
                                        },
                                        placeholder = { if (child.input == "")Text(child.comment) }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.width(56.dp).height(112.dp))
                }
            }
        }
    }
}
package com.fagougou.government.contractPage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.component.Header
import com.fagougou.government.contractPage.ContractViewModel.ContractLists
import com.fagougou.government.contractPage.ContractViewModel.categoryList
import com.fagougou.government.contractPage.ContractViewModel.getContractList
import com.fagougou.government.contractPage.ContractViewModel.getTemplate
import com.fagougou.government.contractPage.ContractViewModel.searchWord
import com.fagougou.government.contractPage.ContractViewModel.selectedId
import com.fagougou.government.model.ContractCategory
import com.fagougou.government.model.ContractData
import com.fagougou.government.model.ContractListRequest
import com.fagougou.government.model.PdfFile
import com.fagougou.government.CommonApplication.Companion.presentation
import com.fagougou.government.repo.Client.contractService
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ContractViewModel{
    val categoryList = mutableStateListOf<ContractCategory>()
    val ContractLists = mutableStateListOf<ContractData>()
    var selectedId = mutableStateOf("")
    val searchWord = mutableStateOf("")
    val BaseLoadUrl="http://beta.products.fagougou.com/api/contract-template/pdf-stream/"
    var pdfFile: PdfFile? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = contractService.listCategory().execute()
                val body = response.body()
                if (body!=null) categoryList.addAll(body.categorys)
                categoryList.reverse()
                selectedId.value = categoryList.firstOrNull()?.id ?: return@launch
                getContractList( selectedId.value )
            }catch (e:Exception){
                handleException(e)
            }
        }
    }

    suspend fun  getContractList(folder:String, searchName:String = "") {
        withContext(Dispatchers.IO) {
            try {
                if (searchName!="")selectedId.value = ""
                val response = contractService.getContractList(ContractListRequest(folder = folder, name = searchName)).execute()
                val body = response.body() ?: return@withContext
                ContractLists.clear()
                ContractLists.addAll(body.data.list)
            }catch (e:Exception){
                handleException(e)
            }
        }
    }

    fun  getTemplate(category :ContractData, navController: NavController) {
        pdfFile = PdfFile(category)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                navController.navigate(Router.contractWebView)
            }
        }
    }
}


@Composable
fun Contract(navController: NavController,category: ContractData){
    Column(
        Modifier.clickable {
            getTemplate(category, navController)
        }
    ){
        Row(
            Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(painterResource(R.drawable.ic_word), null)
            Text(
                category.name,
                Modifier.padding(start = 8.dp),
                fontWeight= FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
        }
        Text(
            category.howToUse ?: "暂无数据",
            Modifier.padding(top = 12.dp),
            fontSize = 20.sp,
            lineHeight = 36.sp,
            color = Color.Black
        )
        Text(
            "行业类型：" + (category.folder?.name ?: "暂无数据"),
            Modifier.padding(top = 12.dp,bottom = 16.dp),
            fontSize = 18.sp,
            color = Color.Gray
        )
        Surface(
            Modifier
                .fillMaxWidth()
                .height(1.dp),color = Color.LightGray
        ){}
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun ContractGuidePage(navController: NavController) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(null){
        presentation?.playVideo(R.raw.vh_contract)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Header(title = "合同文库", navController = navController)
        Surface(
            Modifier
                .height(264.dp)
                .width(1280.dp), color = Color.Transparent
        ) {
            Image(painterResource(id = R.drawable.contract_banner),null)
            Column(
                Modifier
                    .height(264.dp)
                    .width(1280.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "海量合同文库", fontSize = 40 .sp, color = Color.White,fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val (text,button) = remember{ FocusRequester.createRefs() }
                    val textFieldColors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFFFFFFF),
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                    )
                    TextField(
                        searchWord.value,
                        { searchWord.value = it },
                        Modifier
                            .width(568.dp)
                            .height(64.dp)
                            .focusRequester(text)
                            .focusable(),
                        textStyle = TextStyle(color = Color.Gray, fontSize = 24.sp),
                        placeholder = {Text("输入关键词搜索",color = Color.Gray, fontSize = 24.sp)},
                        colors = textFieldColors,
                        shape = RoundedCornerShape(topStart = CORNER_FLOAT, bottomStart = CORNER_FLOAT),
                        maxLines = 1
                    )
                    Button(
                        modifier = Modifier
                            .width(152.dp)
                            .height(64.dp)
                            .focusRequester(button)
                            .focusable(),
                        colors = ButtonDefaults.buttonColors(Dodgerblue),
                        shape = RoundedCornerShape(topEnd = CORNER_FLOAT, bottomEnd = CORNER_FLOAT),
                        elevation = ButtonDefaults.elevation(0.dp),
                        content = {
                            Row( verticalAlignment = Alignment.CenterVertically ){
                                Image(painterResource(R.drawable.ic_search),null)
                                Text(
                                    modifier = Modifier.padding(start = 12.dp),
                                    text = "搜索",
                                    fontSize = 24.sp,
                                    color = Color.White
                                )
                            }
                        },
                        onClick = {
                            text.freeFocus()
                            button.requestFocus()
                            scope.launch { getContractList("",searchWord.value)}
                        }
                    )
                }
            }
        }
        Row(Modifier.fillMaxSize()) {
            Surface(color = Color(0xFFFFFFFF)) {
                Column(Modifier.fillMaxHeight().fillMaxWidth(0.25f),
                ) {
                    LazyColumn(
                        Modifier.padding(vertical = 24.dp),
                        content = {
                            items(categoryList){ category ->
                                Surface(
                                    color = if(category.id == selectedId.value)Color(0xFFBBCCEE) else Color(0xFFFFFFFF)
                                ) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedId.value = category.id
                                                scope.launch { getContractList(selectedId.value) }
                                            },
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Image(
                                            painter = if(category.id== selectedId.value) painterResource(R.drawable.ic_cates) else painterResource(R.drawable.ic_cates_unselected),
                                            contentDescription = null,
                                            modifier = Modifier.padding(start = 24.dp))
                                        Text(
                                            category.name,
                                            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                            fontSize = 22.sp,
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
            Surface(
                Modifier
                    .width(1.dp)
                    .fillMaxHeight(),color = Color.LightGray) {}
            Surface(color = Color(0xFFFFFFFF)) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = {
                        item{
                            Surface(
                                Modifier
                                    .width(8.dp)
                                    .height(8.dp),color = Color.Transparent) { }
                        }
                        items(ContractLists){ category -> Contract(navController, category) }
                        item{
                            if(ContractLists.isEmpty()){
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(512.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Image(painterResource(R.drawable.ic_no_contract),"No Content")
                                    Text(modifier = Modifier.padding(top = 16.dp),text = "暂无搜索结果",fontSize = 28.sp,color = Color.Gray)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

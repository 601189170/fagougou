package com.fagougou.government.contractPage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fagougou.government.component.Header
import com.fagougou.government.R
import com.fagougou.government.Router
import com.fagougou.government.contractPage.Contract.ContractLists
import com.fagougou.government.contractPage.Contract.categoryList
import com.fagougou.government.contractPage.Contract.searchWord
import com.fagougou.government.contractPage.Contract.getContractList
import com.fagougou.government.contractPage.Contract.getTemplate
import com.fagougou.government.contractPage.Contract.selectedId
import com.fagougou.government.contractPage.ContractWebView.codeUrl
import com.fagougou.government.model.ContractCategory
import com.fagougou.government.model.ContractData
import com.fagougou.government.model.ContractListRequest
import com.fagougou.government.repo.Client.contractService
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.ui.theme.CORNER_FLOAT
import com.fagougou.government.ui.theme.Dodgerblue
import com.fagougou.government.qrCode.QrCodeViewModel.currentUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URLEncoder

object Contract{
    val categoryList = mutableStateListOf<ContractCategory>()
    val ContractLists = mutableStateListOf<ContractData>()
    var selectedId = mutableStateOf("")
    val searchWord = mutableStateOf("")

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
        ContractLists.clear()
        withContext(Dispatchers.IO) {
            try {
                if (searchName!="")selectedId.value = ""
                val response = contractService.getContractList(ContractListRequest(folder = folder, name = searchName)).execute()
                val body = response.body() ?: return@withContext
                ContractLists.addAll(body.data.list)
            }catch (e:Exception){
                handleException(e)
            }
        }
    }

    fun  getTemplate(fileid:String,navController: NavController) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = contractService.getTemplate(fileid).execute()
            val body = response.body() ?: return@launch
            withContext(Dispatchers.Main){
                codeUrl = body.data
                currentUrl = body.data
                val encodedUrl = URLEncoder.encode(codeUrl,"UTF-8")
                ContractWebView.webViewUrl = "https://view.officeapps.live.com/op/view.aspx?src=$encodedUrl"
                navController.navigate(Router.contractWebView)
            }
        }
    }
}

@Composable
fun Contract(navController: NavController,category: ContractData){
    Column(
        modifier = Modifier
            .clickable { getTemplate(category.fileid, navController) }
    ){
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            Image(painterResource(R.drawable.ic_word), null)
            Text(
                modifier = Modifier.padding(start = 8.dp),
                fontWeight= FontWeight.Bold,
                text = category.name,
                fontSize = 24.sp,
                color = Color.Black
            )
        }
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = category.howToUse ?: "暂无数据",
            fontSize = 21.sp,
            lineHeight = 36.sp,
            color = Color.Black
        )
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = "行业类型：" + (category.folder?.name ?: "暂无数据"),
            fontSize = 18.sp,
            color = Color.Gray
        )
        Divider(thickness = 2.dp)
    }
}

@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun ContractGuidePage(navController: NavController) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Header(title = "合同文库", navController = navController)
        Surface(
            modifier = Modifier
                .height(264.dp)
                .width(1280.dp),
            color = Color.Transparent
        ) {
            Image(painterResource(id = R.drawable.contract_banner),null)
            Column(
                modifier = Modifier
                    .height(264.dp)
                    .width(1280.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = "海量合同文库", fontSize = 40 .sp, color = Color.White,fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val textFieldColors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFFFFFFF),
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                    )
                    TextField(
                        modifier = Modifier.width(568.dp),
                        value =searchWord.value,
                        onValueChange = { searchWord.value = it },
                        textStyle = TextStyle(color = Color.Gray, fontSize = 24.sp),
                        placeholder = {Text("输入关键词搜索",color = Color.Gray, fontSize = 24.sp)},
                        colors = textFieldColors,
                        shape = RoundedCornerShape(topStart = CORNER_FLOAT, bottomStart = CORNER_FLOAT),
                        maxLines = 1
                    )
                    Button(
                        modifier = Modifier.width(152.dp),
                        colors = ButtonDefaults.buttonColors(Dodgerblue),
                        shape = RoundedCornerShape(topEnd = CORNER_FLOAT, bottomEnd = CORNER_FLOAT),
                        elevation = ButtonDefaults.elevation(0.dp),
                        content = {
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = "搜索",
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        },
                        onClick = { scope.launch { getContractList("",searchWord.value)} }
                    )
                }
            }
        }
        Row(Modifier.fillMaxSize()) {
            Surface(color = Color(0xFFFFFFFF)) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.25f),
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        content = {
                            items(categoryList){ category ->
                                Surface(
                                    color = if(category.id== selectedId.value)Color(0xFFBBCCEE) else Color(0xFFFFFFFF)
                                ) {
                                    Row(
                                        modifier = Modifier
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
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                            text = category.name,
                                            fontSize = 24.sp,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
            Surface(color = Color(0xFFDCE1E6),
                    modifier = Modifier
                    .width(1.dp).fillMaxHeight()
            ) {

            }
            Surface(color = Color(0xFFFFFFFF)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    content = {
                        items(ContractLists){ category ->
                            Contract(navController, category)
                        }
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
                                    Text(text = "暂无相关内容",fontSize = 28.sp,color = Color.Gray)
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

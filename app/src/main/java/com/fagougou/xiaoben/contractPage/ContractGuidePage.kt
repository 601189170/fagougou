package com.fagougou.xiaoben.contractPage

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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
import com.fagougou.xiaoben.contractPage.Contract.getHTLIST
import com.fagougou.xiaoben.contractPage.Contract.getTemplate
import com.fagougou.xiaoben.contractPage.Contract.isfirst

import com.fagougou.xiaoben.contractPage.Contract.weburl
import com.fagougou.xiaoben.model.*
import com.fagougou.xiaoben.repo.Client
import com.fagougou.xiaoben.repo.Client.contractService

import com.fagougou.xiaoben.webViewPage.WebViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.net.URLEncoder


object Contract{
    val categoryList = mutableStateListOf<ContractCategoryResponse2.Data>()
    val HTLists = mutableStateListOf<HTList.DataB>()


    var weburl =mutableStateOf("")
    var isfirst=false;

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val response = contractService.listCategory().execute()
            val body = response.body()
            if (body!=null){
                categoryList.addAll(body.data)
            }
        }

    }


    fun  getHTLIST(folder:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val map = mutableMapOf<String, String>()
            map["name"] = ""
            map["folder"] = folder
            map["limit"] = "20"
            map["skip"] = "0"

            val  req=
                RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(),com.alibaba.fastjson.JSON.toJSONString(map))
            val response = Client.contractService.getHtlist(req).execute()
            val body = response.body() ?: return@launch
            HTLists.clear()
            HTLists.addAll(body.data.list)
        }

    }

    fun  getTemplate(fileid:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = Client.contractService.template(fileid).execute()
            val body = response.body() ?: return@launch
            weburl.value=body.data

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

                        modifier = Modifier.padding(48.dp),

                        content = {

                            items(categoryList){
                                    category ->
                                    Button(
                                        content = {
                                            Text(
//                                                modifier = Modifier.padding(end = 24.dp),
                                                text = category.name,
                                                fontSize = 32.sp,
                                                color = Color.Black

                                            )

                                        },
                                        onClick = {

                                                getHTLIST(category.id)

                                        }
                                    )
                                for (child in category.children) {
                                    Button(
                                        content = {
                                            Text(
//                                                modifier = Modifier.padding(end = 24.dp),
                                                text = child.name,
                                                fontSize = 32.sp,
                                                color = Color.Black

                                            )

                                        },
                                        onClick = {

                                                getHTLIST(child.id)

                                        }
                                    )
                                    for (child in child.children) {
                                        Button(
                                            content = {
                                                Text(
//                                                    modifier = Modifier.padding(end = 24.dp),
                                                    text = child.name,
                                                    fontSize = 32.sp,
                                                    color = Color.Black


                                                )

                                            },
                                            onClick = {

                                                    getHTLIST(child.id)

                                            }
                                        )
                                    }
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
                        .fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.padding(48.dp),
                        content = {
                            items(HTLists){
                                    category ->
                                Button(
                                    content = {
                                        Text(
                                            text = category.name,
                                            fontSize = 32.sp,
                                            color = Color.Black
                                        )
                                    },
                                    onClick = {
                                        getTemplate(category.fileid)

//                                        WebViewModel.WebViewUrl = weburl.value
//                                        Log.e("TAG", "WebViewUrl: "+weburl.value )
//                                        navController.navigate("WebView")

                                    }

                                )
                            }

                            item(weburl.value){
//
                                if (!weburl.value.equals("")&&!isfirst){
                                    isfirst=true
                                    WebViewModel.WebViewUrl = weburl.value
                                    val rawUrl =  weburl.value
                                    val encodedUrl = URLEncoder.encode(rawUrl,"UTF-8")
                                    WebViewModel.WebViewUrl = "https://view.officeapps.live.com/op/view.aspx?src=$encodedUrl"
                                    navController.navigate("WebView")

                                }

                            }
                        }



                    )

                }

            }

        }

    }


}

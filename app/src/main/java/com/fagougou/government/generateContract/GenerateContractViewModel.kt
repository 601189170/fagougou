package com.fagougou.government.generateContract

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.fagougou.government.component.QrCodeViewModel
import com.fagougou.government.model.*
import com.fagougou.government.repo.Client
import com.fagougou.government.utils.Time
import com.fagougou.government.utils.Tips
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.IOException
import java.io.InputStreamReader

object GenerateContractViewModel {
    val contractList = mutableStateListOf<GenerateContractBrief>()
    val currentContractId = mutableStateOf("")
    var baseHtml = ""
    var template = ""
    var handleBarsResult = ""
    val data = mutableStateOf("")
    val formList = mutableStateListOf(GenerateForm())
    var lastModifier = Time.stamp.toString()

    fun init(context: Context) {
        val file = context.assets.open("html/generateContract.html")
        baseHtml = InputStreamReader(file, "UTF-8").readText()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = Client.generateService.getGeneratelist().execute()
                val body = response.body()?.data ?: GenerateContractListResponse().data
                contractList.addAll(body)
            } catch (e: Exception) {
                Client.handleException(e)
            }
        }
    }

    fun clear() {
        currentContractId.value = ""
        template = ""
        data.value = ""
        formList.clear()
        lastModifier = Time.stamp.toString()
    }

    suspend fun getGenerateForm(id: String) {
        withContext(Dispatchers.IO) {
            formList.clear()
            try {
                val response = Client.generateService.getGenrateForm(id).execute()
                val body = response.body()?.data ?: GenerateData()
                formList.addAll(body.forms)
            } catch (e: Exception) {
                Client.handleException(e)
            }
        }
    }

    suspend fun getGenerateTemplate(id: String) {
        withContext(Dispatchers.IO) {
            template = ""
            try {
                val response = Client.generateService.getGenrateTemplete(id).execute()
                val body = response.body()?.data ?: GenerateContractTemplete()
                template = body.content
            } catch (e: Exception) {
                Client.handleException(e)
            }
        }
    }

    suspend fun updateContent() {
        withContext(Dispatchers.Default) {
            val builder = StringBuilder()
            for (item in formList) {
                for (child in item.children) {
                    with(child) {
                        val result = when (type) {
                            "checkbox" -> {
                                if (selected.isNotEmpty()) {
                                    val builder = StringBuilder()
                                    for (select in selected) {
                                        builder.append(values[select])
                                    }
                                    val result = builder.toString()
                                    if (result.isNotEmpty()) result.substring(0, builder.lastIndex)
                                    else ""
                                } else ""
                            }
                            "select" -> {
                                if (selected.isNotEmpty()) values[selected.first()] else ""
                            }
                            else -> if (input.value != "") input.value else "__________"
                        }
                        builder.append("${variable}:\"$result\",")
                    }
                }
            }
            val result = baseHtml
                .replace("{{TemplateHook}}", template)
                .replace("{{DataHook}}", builder.toString())
                .replace(
                    "class=\"$lastModifier",
                    "style=\"background-color: yellow;\" class=\"$lastModifier"
                )
            data.value = result
        }
    }

    fun html2Doc(fileName:String, htmlString: String){
        val resultString = htmlString
            .substring(1,htmlString.length-1)
            .replace("\\n","")
            .replace("\\u003C","<")
        val fileBody = resultString.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", fileName, fileBody)
            .build();
        val request = Request.Builder().url("https://products.fagougou.com/api/convert/from/html/to/docx").post(requestBody).build()
        Client.justLoadClient.newCall(request).enqueue(Html2DocCallback())
    }
}
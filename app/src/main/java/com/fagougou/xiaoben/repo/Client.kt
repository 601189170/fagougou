package com.fagougou.xiaoben.repo

import android.net.ParseException
import androidx.compose.runtime.mutableStateOf
import com.bugsnag.android.Bugsnag
import com.fagougou.xiaoben.repo.Client.globalLoading
import com.fagougou.xiaoben.repo.Client.handleException
import com.fagougou.xiaoben.utils.MMKV.kv
import com.fagougou.xiaoben.utils.Tips.toast
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLHandshakeException

class CommonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Referer","https://www.fagougou.com/pc/?mkt=fefil0763c92e")
            .addHeader("Authorization", kv.decodeString("token") ?: "")
            .addHeader("inner-token",kv.decodeString("contractToken") ?: "")
            .method(original.method, original.body)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

class ParametersIntercept : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        globalLoading.value++
        val request: Request = chain.request()
        val response = chain.proceed(request)
        globalLoading.value--
        if(response.code == 200 && response.body!=null){
            try{
                var bodyString = response.body!!.string()
                bodyString = bodyString
                    .replace(",\"option\":[]","")
                    .replace("\"option\":[{\"title\"","\"option\":{\"title\"")
                    .replace("\"}]},\"isAnswered\"","\"}},\"isAnswered\"")
                    .replace("\"}]}}],\"bestScore\"","\"}}}],\"bestScore\"")
                    .replace(" ","")
                    .replace("\n","")
                val contentType = response.body!!.contentType()
                val body = ResponseBody.create(contentType,bodyString)
                return response.newBuilder().body(body).build()
            }catch (e:Exception){
                handleException(e)
            }
        }
        return response
    }
}

object Client {
    var globalLoading = mutableStateOf(0)
    const val url = "https://api.fagougou.com"
    const val contractUrl = "https://law-system.fagougou-law.com"
    const val loginUrl = "https://a.fagougou.com"
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(ParametersIntercept())
            .addInterceptor(CommonInterceptor())
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val contractService: ContractService by lazy {
        Retrofit.Builder()
            .baseUrl(contractUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContractService::class.java)
    }

    val mainLogin: MainLogin by lazy {
        Retrofit.Builder()
            .baseUrl(loginUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MainLogin::class.java)
    }

    fun handleException(t: Throwable) {
        globalLoading.value--
        val ex: Exception
        when (t) {
            is HttpException -> ex = Exception("服务器错误")
            is JsonParseException, is JSONException, is ParseException -> ex = Exception("解析错误")
            is SocketException -> ex = Exception("网络连接错误，请重试")
            is SocketTimeoutException -> ex = Exception("网络连接超时")
            is SSLHandshakeException -> ex = Exception("证书验证失败")
            is UnknownHostException -> ex = Exception("网络错误，请切换网络重试")
            is UnknownServiceException -> ex = Exception("网络错误，请切换网络重试")
            is NumberFormatException -> ex = Exception("数字格式化异常")
            else -> {
                ex = t as Exception
                Bugsnag.notify(ex)
            }
        }
        CoroutineScope(Dispatchers.Main).launch{
            toast(ex.message ?: "")
        }
    }
}
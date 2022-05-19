package com.fagougou.government.repo

import android.net.ParseException
import androidx.compose.runtime.mutableStateOf
import com.bugsnag.android.Bugsnag
import com.fagougou.government.repo.Client.globalLoading
import com.fagougou.government.repo.Client.handleException
import com.fagougou.government.utils.MMKV.kv
import com.fagougou.government.utils.Tips.toast
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
import java.util.concurrent.CancellationException
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
        val request: Request = chain.request()
        val response = chain.proceed(request)
        if(response.code == 200 && response.body!=null){
            try{
                var bodyString = response.body!!.string()
                bodyString = bodyString
                    .replace(",\"option\":[]","")
                    .replace("\"option\":[{\"title\"","\"option\":{\"title\"")
                    .replace("\"}]},\"isAnswered\"","\"}},\"isAnswered\"")
                    .replace("\"}]}}],\"bestScore\"","\"}}}],\"bestScore\"")
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

class BlockIntercept : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        globalLoading.value++
        val request: Request = chain.request()
        val response = chain.proceed(request)
        globalLoading.value--
        return response
    }
}

object Client {
    var globalLoading = mutableStateOf(0)
    const val url = "https://api.fagougou.com"
    const val contractUrl = "https://law-system.fagougou-law.com/"
    const val registerUrl = "http://test.robot-manage-system.fagougou.com/"
    const val updateUrl = "https://fagougou-1251511189.cos.ap-nanjing.myqcloud.com"
    const val generateUrl = "https://products.fagougou.com/api/"
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(BlockIntercept())
            .addInterceptor(ParametersIntercept())
            .addInterceptor(CommonInterceptor())
            .callTimeout(12, TimeUnit.SECONDS)
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
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

    val mainRegister: MainRegister by lazy {
        Retrofit.Builder()
            .baseUrl(registerUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MainRegister::class.java)
    }

    val updateService: UpdateService by lazy {
        Retrofit.Builder()
            .baseUrl(updateUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UpdateService::class.java)
    }

    val generateService: GenerateService by lazy {
        Retrofit.Builder()
            .baseUrl(generateUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GenerateService::class.java)
    }

    fun handleException(t: Throwable) {
        globalLoading.value--
        val ex: Exception
        when (t) {
            is CancellationException -> return
            is HttpException -> ex = Exception("服务器错误")
            is JsonParseException, is JSONException, is ParseException -> ex = Exception("解析错误")
            is SocketException -> ex = Exception("无网络连接")
            is SocketTimeoutException -> ex = Exception("网络连接超时")
            is SSLHandshakeException -> ex = Exception("证书验证失败")
            is UnknownHostException -> ex = Exception("无网络连接")
            is UnknownServiceException -> ex = Exception("无网络连接")
            is NumberFormatException -> ex = Exception("数字格式化异常")
            else -> {
                ex = t as Exception
                Bugsnag.notify(ex)
            }
        }
        CoroutineScope(Dispatchers.Main).launch{
            if (!ex.message.isNullOrBlank())toast(ex.message?:"")
        }
    }
}
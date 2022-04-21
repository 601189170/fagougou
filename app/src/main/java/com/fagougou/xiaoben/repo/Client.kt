package com.fagougou.xiaoben.repo

import android.net.ParseException
import com.fagougou.xiaoben.utils.MMKV.kv
import com.fagougou.xiaoben.utils.Tips.toast
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
            .method(original.method, original.body)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

object Client {
    const val url = "https://api.fagougou.com"
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(CommonInterceptor())
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val retrofitClient: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun handleException(t: Throwable) {
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
            }
        }
        CoroutineScope(Dispatchers.Main).launch{
            toast(ex.message ?: "")
        }
    }
}
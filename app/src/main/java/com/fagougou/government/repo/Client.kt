package com.fagougou.government.repo

import android.net.ParseException
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.bugsnag.android.Bugsnag
import com.fagougou.government.repo.interceptor.BlockIntercept
import com.fagougou.government.repo.interceptor.CommonAuthInterceptor
import com.fagougou.government.repo.interceptor.ParametersIntercept
import com.fagougou.government.utils.Tips.toast
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InterruptedIOException
import java.lang.Integer.max
import java.net.SocketException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLHandshakeException

object Client {
    var globalLoading = mutableStateOf(0)
    const val url = "https://api.fagougou.com/"
    const val contractUrl = "https://law-system.fagougou-law.com/"
    const val registerUrl = "http://test.robot-manage-system.fagougou.com/"
    const val updateUrl = "https://fagougou-1251511189.cos.ap-nanjing.myqcloud.com/"
    const val generateUrl = "https://products.fagougou.com/api/"
    const val serverlessUrl = "https://hwc.infiiinity.xyz/"
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(BlockIntercept(globalLoading))
            .addInterceptor(ParametersIntercept())
            .addInterceptor(CommonAuthInterceptor())
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)
            .build()
    }

    val noLoadClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(CommonAuthInterceptor())
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)
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

    val serverlessService: ServerlessService by lazy {
        Retrofit.Builder()
            .baseUrl(serverlessUrl)
            .client(noLoadClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerlessService::class.java)
    }

    fun handleException(t: Throwable) {
        val e: Exception
        when (t) {
            is CancellationException -> return
            is InterruptedIOException -> e = Exception("网络连接超时")
            is HttpException -> e = Exception("服务器错误")
            is JsonParseException, is JSONException, is ParseException -> e = Exception("解析错误")
            is SocketException -> e = Exception("无网络连接")
            is SSLHandshakeException -> e = Exception("证书验证失败")
            is UnknownHostException -> e = Exception("无网络连接")
            is UnknownServiceException -> e = Exception("无网络连接")
            is NumberFormatException -> e = Exception("数字格式化异常")
            else -> {
                e = t as Exception
                Bugsnag.notify(e)
            }
        }
        CoroutineScope(Dispatchers.Main).launch{
            if (!e.message.isNullOrBlank())toast(e.message?:"")
        }
    }

    fun <T> callBack(onSuccess:(T)->Unit): retrofit2.Callback<T> {
        return object:retrofit2.Callback<T>{
            override fun onResponse(call: retrofit2.Call<T>, response: retrofit2.Response<T>) {
                response.body()?.let { onSuccess.invoke(it) }
            }
            override fun onFailure(call: retrofit2.Call<T>, t: Throwable) {
                handleException(t)
            }
        }
    }

    fun MutableState<Int>.pop(){
        val it = max(value-1,0)
        value = it
    }
}
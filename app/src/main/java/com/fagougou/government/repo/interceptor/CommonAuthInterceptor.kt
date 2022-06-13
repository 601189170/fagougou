package com.fagougou.government.repo.interceptor

import com.fagougou.government.utils.MMKV
import okhttp3.Interceptor
import okhttp3.Response

class CommonAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .addHeader("Referer","https://www.fagougou.com/pc/?mkt=fefil0763c92e")
            .addHeader("Authorization", MMKV.kv.decodeString("token") ?: "")
            .method(original.method, original.body)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
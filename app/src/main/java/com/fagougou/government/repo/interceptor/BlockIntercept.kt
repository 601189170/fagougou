package com.fagougou.government.repo.interceptor

import com.fagougou.government.repo.Client
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class BlockIntercept : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Client.globalLoading.value++
        val request: Request = chain.request()
        val response = chain.proceed(request)
        Client.globalLoading.value--
        return response
    }
}
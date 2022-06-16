package com.fagougou.government.repo.interceptor

import com.fagougou.government.repo.Client
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception
import java.lang.Integer.max

class BlockIntercept : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Client.globalLoading.value++
        val request: Request = chain.request()
        try {
            return chain.proceed(request)
        } catch (e: Exception) {
            throw e
        } finally {
            val it = Client.globalLoading.value - 1
            Client.globalLoading.value = max(0, it)
        }
    }
}
package com.fagougou.government.repo.interceptor

import androidx.compose.runtime.MutableState
import com.fagougou.government.repo.Client
import com.fagougou.government.repo.Client.pop
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception
import java.lang.Integer.max

class BlockIntercept(val loading:MutableState<Int>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        loading.value++
        val request: Request = chain.request()
        try {
            return chain.proceed(request)
        } catch (e: Exception) {
            throw e
        } finally {
            loading.pop()
        }
    }
}
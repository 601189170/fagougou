package com.fagougou.government.repo.interceptor

import com.fagougou.government.repo.Client
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

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
                Client.handleException(e)
            }
        }
        return response
    }
}
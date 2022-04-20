package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.About
import com.fagougou.xiaoben.model.Auth
import com.fagougou.xiaoben.model.AuthRequest
import com.fagougou.xiaoben.model.BotList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {
    @POST("/api/component/ABOUT_ROBOT")
    fun aboutRobot(): Call<About>

    @Headers("Content-Type: application/json")
    @POST("auth/accessToken")
    fun auth(@Body authRequest: AuthRequest): Call<Auth>

    @Headers("Content-Type: application/json")
    @GET("v1/chatbot/products")
    fun botList(): Call<BotList>
}
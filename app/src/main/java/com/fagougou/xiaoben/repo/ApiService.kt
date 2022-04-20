package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("/api/component/ABOUT_ROBOT")
    fun aboutRobot(): Call<About>

    @POST("/auth/accessToken")
    fun auth(@Body authRequest: AuthRequest): Call<Auth>

    @GET("/v1/chatbot/products")
    fun botList(): Call<BotList>

    @POST("/robot/chatbot/{id}/query")
    fun startChat(@Path("id")botId:String): Call<StartChatResult>
}
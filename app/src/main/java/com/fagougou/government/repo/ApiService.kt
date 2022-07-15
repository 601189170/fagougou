package com.fagougou.government.repo

import com.fagougou.government.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/component/ABOUT_ROBOT")
    fun aboutRobot(): Call<About>

    @POST("auth/accessToken")
    fun auth(@Body authRequest: AuthRequest): Call<Auth>

    @GET("v1/chatbot/products")
    fun botList(): Call<BotList>

    @POST("robot/chatbot/{id}/query")
    fun startChat(@Path("id") botId: String): Call<ChatResponse>

    @POST("robot/query/{sessionId}/chat")
    fun nextChat(
        @Path("sessionId") sessionId: String,
        @Body message: ChatRequest
    ): Call<ChatResponse>

    @GET("v1/qritem/{queryRecordItemId}/related-qas")
    fun relateQuestion(@Path("queryRecordItemId") queryRecordItemId: String): Call<RelateResponse>

    @GET("v1/attachment/{attachmentId}")
    fun attachment(@Path("attachmentId") attachmentId: String): Call<AttachmentResponse>

    @GET("v1/case-origin/{serial}")
    fun case(
        @Path("serial") serial: String
    ): Call<CaseResponse>

    @POST("v1/query/{sessionId}/define")
    fun define(
        @Path("sessionId") sessionId: String,
        @Body request: DefineRequest
    ): Call<DefineResponse>

}
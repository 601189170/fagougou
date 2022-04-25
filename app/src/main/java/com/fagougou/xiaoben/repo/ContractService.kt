package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.*
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.*

interface ContractService {
    @POST("/contract-template/category/list")
    fun listCategory(): Call<ContractCategoryResponse>

//    @POST("/auth/accessToken")
//    fun auth(@Body authRequest: AuthRequest): Call<Auth>
//
//    @GET("/v1/chatbot/products")
//    fun botList(): Call<BotList>
//
//    @POST("/robot/chatbot/{id}/query")
//    fun startChat(@Path("id")botId:String): Call<ChatResponse>
//
//    @POST("/robot/query/{sessionId}/chat")
//    fun nextChat(@Path("sessionId")sessionId:String, @Body message:ChatRequest): Call<ChatResponse>
//
//    @GET("/v1/qritem/{queryRecordItemId}/related-qas")
//    fun relateQuestion(@Path("queryRecordItemId")queryRecordItemId:String): Call<RelateResponse>
//
//    @GET("/v1/attachment/{attachmentId}")
//    fun attachment(@Path("attachmentId")attachmentId:String): Call<AttachmentResponse>
}
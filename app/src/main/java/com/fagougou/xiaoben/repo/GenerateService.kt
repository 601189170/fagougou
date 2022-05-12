package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.*
import retrofit2.Call
import retrofit2.http.*

interface GenerateService {
    @POST("contractTemplateBycategory")
    fun getGeneratelist(@Body request: GenerateListRequest): Call<GenerateContractListResponse>

    @GET("getTemplate/{id}")
    fun getGenrateTemplete(@Path("id") id :String): Call<GenerateContractTempleteResponse>

    @GET("getTemplateForms/{id}")
    fun getGenrateForm(@Path("id") id :String): Call<GenerateContractResponse>
}
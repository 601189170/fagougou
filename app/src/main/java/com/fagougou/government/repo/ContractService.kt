package com.fagougou.government.repo

import com.fagougou.government.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ContractService {
    @GET("contractTemplateCates")
    fun listCategory(): Call<ContractCategoryResponse>

    @POST("contractTemplate/list")
    fun getContractList(
        @Body request:ContractListRequest
    ): Call<ContractList>

    @Multipart
    @POST("/api/contract-audit/out/upload")
    suspend fun upload(@Part part:  MultipartBody.Part ): Call<ContractUrlBean>
}
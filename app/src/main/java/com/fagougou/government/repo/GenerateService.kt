package com.fagougou.government.repo

import com.fagougou.government.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface GenerateService {
    @GET("contractTemplates")
    fun getGeneratelist(): Call<GenerateContractListResponse>

    @GET("getTemplate/{id}")
    fun getGenrateTemplete(@Path("id") id :String): Call<GenerateContractTempleteResponse>

    @GET("getTemplateForms/{id}")
    fun getGenrateForm(@Path("id") id :String): Call<GenerateContractResponse>

    @Multipart
    @POST("convert/from/html/to/docx")
    fun getDocFile(@Part part: MultipartBody.Part): Call<String>
}
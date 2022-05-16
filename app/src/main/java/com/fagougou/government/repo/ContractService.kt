package com.fagougou.government.repo

import com.fagougou.government.model.*
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Query

interface ContractService {
    @GET("contractTemplateCates")
    fun listCategory(): Call<ContractCategoryResponse>


    @POST("contractTemplate/list")
    fun getContractList(@Body request:ContractListRequest): Call<ContractList>


    @GET("contractTemplate/downloadLink")
    fun getTemplate(@Query("fileid") fileid :String): Call<TemplateBean>

}
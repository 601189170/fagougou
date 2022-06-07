package com.fagougou.government.repo

import com.fagougou.government.model.ContractCategoryResponse
import com.fagougou.government.model.ContractList
import com.fagougou.government.model.ContractListRequest
import com.fagougou.government.model.TemplateBean
import retrofit2.Call
import retrofit2.http.*

interface ContractService {
    @GET("contractTemplateCates")
    fun listCategory(): Call<ContractCategoryResponse>

    @POST("contractTemplate/list")
    fun getContractList(@Body request:ContractListRequest): Call<ContractList>

    @GET("contractTemplate/downloadLink")
    fun getTemplate(@Query("fileid") fileid :String): Call<TemplateBean>
}
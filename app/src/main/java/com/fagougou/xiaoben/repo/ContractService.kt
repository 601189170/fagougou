package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.*
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Query

interface ContractService {
    @GET("/contractTemplateCates")
    fun listCategory(): Call<ContractCategoryResponse>


    @POST("/contractTemplate/list")
    fun getHtlist(@Body hTListRequest:ContractListRequest): Call<ContractList>


    @GET("/contractTemplate/downloadLink")
    fun template(@Query("fileid") fileid :String): Call<TemplateBean>
}
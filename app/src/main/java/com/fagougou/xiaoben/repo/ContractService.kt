package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.*
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.*

interface ContractService {
    @GET("/contractTemplateCates")
    fun listCategory(): Call<ContractCategoryResponse2>


    @POST("/contractTemplate/list")
    fun getHtlist(@Body requestBody: RequestBody): Call<HTList>


    @GET("/contractTemplate/downloadLink")
    fun template(@Query("fileid") fileid :String): Call<TemplateBean>
}
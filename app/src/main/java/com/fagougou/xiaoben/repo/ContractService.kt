package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.*
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.*

interface ContractService {
    @GET("/api/contract-template/category/list")
    fun listCategory(): Call<ContractCategoryResponse>

    @POST("/api/contract-templates")
    fun get(): Call<BotList>
}
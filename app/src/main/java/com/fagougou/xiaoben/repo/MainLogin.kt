package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MainLogin {
    @POST("/register")
    fun register(@Body request: SerialRegiterRequest): Call<SerialRegisterRespon>

    @POST("/login")
    fun login(@Body request: SerialLoginRespon): Call<SerialLoginRespon>
}
package com.fagougou.government.repo

import com.fagougou.government.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MainLogin {
    @POST("/api/register-codes/register/")
    fun register(@Body request: SerialRegisterRequest): Call<SerialRegisterResponse>

    @POST("/api/register-codes/login/")
    fun login(@Body request: SerialLoginRequest): Call<SerialLoginResponse>
}
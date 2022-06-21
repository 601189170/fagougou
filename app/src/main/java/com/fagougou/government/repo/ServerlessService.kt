package com.fagougou.government.repo

import com.fagougou.government.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerlessService {

    @GET("SetHeartBeats")
    fun setHeartBeats(@Query("serial") serial:String,@Query("version") version:String): Call<ResponseBody>

    @GET("GetAdvertise")
    fun getAds(@Query("serial") serial:String): Call<Advertise>

    @GET("GetAboutUs")
    fun getAboutUs(@Query("serial") serial:String): Call<AboutUs>

}
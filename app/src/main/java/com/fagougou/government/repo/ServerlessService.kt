package com.fagougou.government.repo

import com.fagougou.government.model.AboutUs
import com.fagougou.government.model.Advertise
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerlessService {

    @GET("SetHeartBeats")
    fun setHeartBeats(
        @Query("serial") serial:String,
        @Query("version") version:Int,
        @Query("adbPort") adbPort:Int
    ): Call<ResponseBody>

    @GET("GetAdvertise")
    fun getAds(
        @Query("serial") serial:String
    ): Call<Advertise>

    @GET("GetAboutUs")
    fun getAboutUs(
        @Query("serial") serial:String
    ): Call<AboutUs>

}
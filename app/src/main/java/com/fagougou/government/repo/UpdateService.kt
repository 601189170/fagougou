package com.fagougou.government.repo

import com.fagougou.government.model.UpdateInfo
import retrofit2.Call
import retrofit2.http.GET

interface UpdateService {
    @GET("/FagougouAtXiaobenUpdate.json")
    fun updateInfo(): Call<UpdateInfo>
}
package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.UpdateInfo
import retrofit2.Call
import retrofit2.http.GET

interface UpdateService {
    @GET("/FagougouAtXiaobenUpdate.json")
    fun updateInfo(): Call<UpdateInfo>
}
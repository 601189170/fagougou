package com.fagougou.xiaoben.repo

import com.fagougou.xiaoben.model.User
import com.fagougou.xiaoben.model.UserResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MainLogin {
    @POST("/firmLogin")
    fun login(@Body user: User): Call<UserResult>
}
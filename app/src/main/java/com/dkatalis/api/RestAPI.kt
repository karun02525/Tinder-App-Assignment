package com.dkatalis.api


import com.dkatalis.api.callback.UserCallback
import io.reactivex.Single
import retrofit2.http.GET


interface RestAPI {
    @GET("/api/0.4/?randomapi")
    fun getUser(): Single<UserCallback>
}
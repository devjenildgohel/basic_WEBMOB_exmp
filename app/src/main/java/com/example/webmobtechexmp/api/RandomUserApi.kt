package com.example.webmobtechexmp.api

import com.example.webmobtechexmp.model.RandomUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {
    @GET("api")
    fun getUsers(
        @Query("page") page: Int,
        @Query("results") resultsPerPage: Int,
        @Query("seed") seed: String = "randomuser"
    ): Call<RandomUserResponse>
}

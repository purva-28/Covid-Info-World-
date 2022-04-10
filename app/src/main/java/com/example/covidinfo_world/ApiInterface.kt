package com.example.covidinfo_world

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("countries")
    fun getcountrydata(): Call<List<ModelClass?>?>?

    companion object {
        const val BASE_URL = "https://corona.lmao.ninja/v2/"
    }
}
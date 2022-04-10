package com.example.covidinfo_world

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {
    var retrofit: Retrofit? = null
    val aPIInterface: ApiInterface
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(ApiInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit!!.create(ApiInterface::class.java)
        }
}
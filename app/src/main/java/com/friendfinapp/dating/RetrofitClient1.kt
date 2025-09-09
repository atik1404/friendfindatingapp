package com.friendfinapp.dating


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object RetrofitClient1 {
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofitInstance.create(service)
    }
}

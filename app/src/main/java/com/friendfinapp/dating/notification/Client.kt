package com.friendfinapp.dating.notification

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Client {
    //creating retrofit client for fetch data from api
    private var retrofit: Retrofit? = null
    fun getClient(url: String?): Retrofit? {
        if (retrofit == null) {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }
        return retrofit
    }

    //create client instance
    private var mInstance: Client? = null

    @get:Synchronized
    val instance: Client?
        get() {
            if (mInstance == null) mInstance = Client
            return mInstance
        }

    //built http client
    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}
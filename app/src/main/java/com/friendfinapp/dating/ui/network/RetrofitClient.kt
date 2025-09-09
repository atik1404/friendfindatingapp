package com.friendfinapp.dating.ui.network


import com.friendfinapp.dating.helper.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BaseUrl)
       // .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

        .client(okHttpClient())
        .build()
    val api: ApiInterface
        get() = retrofit.create(ApiInterface::class.java)

    companion object {
        private var mInstance: RetrofitClient? = null

        @get:Synchronized
        val instance: RetrofitClient?
            get() {
                if (mInstance == null) mInstance = RetrofitClient()
                return mInstance
            }

        private fun okHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

}
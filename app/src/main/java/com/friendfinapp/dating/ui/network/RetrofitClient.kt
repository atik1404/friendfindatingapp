package com.friendfinapp.dating.ui.network


import com.friendfinapp.dating.helper.Constants
import com.friendfinapp.dating.helper.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class RetrofitClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BaseUrl)
        //.baseUrl(Constants.TestBaseUrl)
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
            val sessionManager = SessionManager.get()
            val token = Constants.AUTHORIZATION_TOKEN.ifEmpty { sessionManager.token }
            val httpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(provideLoggerInterceptor())

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer $token")
                val request = requestBuilder.build()
                chain.proceed(request)
            }

            return httpClient.build();
        }

        fun provideLoggerInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor { message -> Timber.e(message) }
            interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.HEADERS }
            interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
            Timber.e("Authorization Token: ${Constants.AUTHORIZATION_TOKEN}")
            return interceptor
        }
    }

}
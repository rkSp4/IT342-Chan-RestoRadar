package com.chan.restoradar.shared.network

import com.chan.restoradar.feature.auth.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    //private const val BASE_URL = "https://api.restoradar.com/api/v1/"
    private const val BASE_URL = "http://192.168.1.236:8080/api/v1/"
    //private const val BASE_URL = "http://10.57.69.107:8080/api/v1/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthApiService = retrofit.create(AuthApiService::class.java)
}
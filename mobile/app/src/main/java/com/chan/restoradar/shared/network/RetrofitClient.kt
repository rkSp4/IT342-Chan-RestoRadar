package com.chan.restoradar.shared.network

import android.content.Context
import com.chan.restoradar.feature.auth.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // private const val BASE_URL = "https://api.restoradar.com/api/v1/"
    private const val BASE_URL = "http://10.10.10.233:8080/api/v1/"
    // private const val BASE_URL = "http://10.57.69.107:8080/api/v1/"
    // For emulator: "http://10.0.2.2:8080/api/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ── Client WITHOUT auth (for login / register) ────────────────────────────
    private val noAuthClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val noAuthRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(noAuthClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthApiService = noAuthRetrofit.create(AuthApiService::class.java)

    // ── Client WITH JWT auth (for all other screens) ──────────────────────────
    fun <T> createService(context: Context, serviceClass: Class<T>): T {

        val authClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                // Utilizing your existing SessionStore for clean architecture
                val token = SessionStore.getToken(context)

                val requestBuilder = chain.request().newBuilder()

                // Safely append the Authorization header if the token exists
                if (!token.isNullOrBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(serviceClass)
    }
}
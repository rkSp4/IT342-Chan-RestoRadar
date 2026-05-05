package com.chan.restoradar.feature.auth

import com.chan.restoradar.feature.auth.AuthResponse
import com.chan.restoradar.feature.auth.LoginRequest
import com.chan.restoradar.feature.auth.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>
}
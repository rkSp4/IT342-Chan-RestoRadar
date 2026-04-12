package com.chan.restoradar.data.repository

import com.chan.restoradar.data.models.AuthData
import com.chan.restoradar.data.models.LoginRequest
import com.chan.restoradar.data.models.RegisterRequest
import com.chan.restoradar.data.network.RetrofitClient

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
}

class AuthRepository {

    private val api = RetrofitClient.authService

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): AuthResult<AuthData> {
        return try {
            val response = api.register(
                RegisterRequest(email, password, confirmPassword, fullName)
            )

            android.util.Log.d("AUTH", "Register Code: ${response.code()}")
            android.util.Log.d("AUTH", "Register Body: ${response.body()}")
            android.util.Log.d("AUTH", "Register Error: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AuthResult.Success(
                        AuthData(
                            user = body.user,
                            token = body.accessToken,
                            refreshToken = body.refreshToken
                        )
                    )
                } else {
                    AuthResult.Error("Empty response from server")
                }
            } else {
                AuthResult.Error("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            android.util.Log.e("AUTH", "Register Exception: ${e.message}")
            AuthResult.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun login(email: String, password: String): AuthResult<AuthData> {
        return try {
            val response = api.login(LoginRequest(email, password))

            android.util.Log.d("AUTH", "Login Code: ${response.code()}")
            android.util.Log.d("AUTH", "Login Body: ${response.body()}")
            android.util.Log.d("AUTH", "Login Error: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    AuthResult.Success(
                        AuthData(
                            user = body.user,
                            token = body.accessToken,
                            refreshToken = body.refreshToken
                        )
                    )
                } else {
                    AuthResult.Error("Empty response from server")
                }
            } else {
                AuthResult.Error("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            android.util.Log.e("AUTH", "Login Exception: ${e.message}")
            AuthResult.Error(e.localizedMessage ?: "Network error")
        }
    }
}
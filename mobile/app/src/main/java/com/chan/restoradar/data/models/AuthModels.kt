package com.chan.restoradar.data.models

import com.google.gson.annotations.SerializedName

// ── Request Bodies ────────────────────────────────────────────────────────────

data class RegisterRequest(
    @SerializedName("email")           val email: String,
    @SerializedName("password")        val password: String,
    @SerializedName("confirmPassword") val confirmPassword: String,
    @SerializedName("fullName")        val fullName: String
)

data class LoginRequest(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String
)

// ── Response Models (matching your actual backend) ────────────────────────────

// Your backend returns this directly (no success/data wrapper)
data class AuthResponse(
    @SerializedName("user")         val user: UserDto,
    @SerializedName("accessToken")  val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)

data class UserDto(
    @SerializedName("id")           val id: String,
    @SerializedName("email")        val email: String,
    @SerializedName("fullName")     val fullName: String,
    @SerializedName("profileImage") val profileImage: String?,
    @SerializedName("role")         val role: String?,
    @SerializedName("createdAt")    val createdAt: String?,
    @SerializedName("updatedAt")    val updatedAt: String?
)

// ── Internal app model (used by ViewModel/UI) ─────────────────────────────────

data class AuthData(
    val user: UserDto,
    val token: String,
    val refreshToken: String
)
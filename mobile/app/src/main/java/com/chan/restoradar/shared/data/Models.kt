package com.chan.restoradar.shared.data

// ── User ──────────────────────────────────────────────────────────────────────

data class UserProfile(
    val id: String,
    val email: String,
    val fullName: String,
    val role: String? = null,
    val profileImage: String? = null
)

data class UpdateUserRequest(val name: String, val email: String)

// ── Restaurant ────────────────────────────────────────────────────────────────

data class Restaurant(
    val id: String,
    val name: String,
    val description: String? = null,
    val address: String? = null,
    val cuisineType: String? = null,
    val priceRange: String? = null,
    val averageRating: Double? = null,
    val reviewCount: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val imageUrl: String? = null,
    val phoneNumber: String? = null,
    val website: String? = null,
    val openingHours: String? = null
)

// ── Review ────────────────────────────────────────────────────────────────────

data class Review(
    val id: String,
    val restaurantId: String,
    val userId: String,
    val userName: String? = null,
    val rating: Int,
    val comment: String? = null,
    val createdAt: String? = null
)

data class ReviewRequest(val rating: Int, val comment: String)

// ── Favorite ──────────────────────────────────────────────────────────────────

data class Favorite(
    val id: String,
    val restaurantId: String,
    val restaurant: Restaurant? = null
)

data class FavoriteRequest(val restaurantId: String)
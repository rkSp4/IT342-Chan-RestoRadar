package com.chan.restoradar.shared.network

import com.chan.restoradar.shared.data.Favorite
import com.chan.restoradar.shared.data.FavoriteRequest
import com.chan.restoradar.shared.data.Restaurant
import com.chan.restoradar.shared.data.Review
import com.chan.restoradar.shared.data.ReviewRequest
import com.chan.restoradar.shared.data.UpdateUserRequest
import com.chan.restoradar.shared.data.UserProfile
import retrofit2.Response
import retrofit2.http.*

interface RestoRadarApi {

    // ── Restaurants ───────────────────────────────────────────────────────────
    @GET("restaurants")
    suspend fun getRestaurants(
        @Query("cuisineType") cuisineType: String? = null,
        @Query("priceRange") priceRange: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<List<Restaurant>>

    @GET("restaurants/{id}")
    suspend fun getRestaurant(@Path("id") id: String): Response<Restaurant>

    @GET("restaurants/search")
    suspend fun searchRestaurants(@Query("q") query: String): Response<List<Restaurant>>

    @GET("restaurants/nearby")
    suspend fun getNearbyRestaurants(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radiusKm") radiusKm: Double = 5.0
    ): Response<List<Restaurant>>

    // ── Reviews ───────────────────────────────────────────────────────────────
    @GET("restaurants/{id}/reviews")
    suspend fun getReviews(@Path("id") restaurantId: String): Response<List<Review>>

    @POST("restaurants/{id}/reviews")
    suspend fun addReview(
        @Path("id") restaurantId: String,
        @Body request: ReviewRequest
    ): Response<Review>

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Path("id") reviewId: String,
        @Body request: ReviewRequest
    ): Response<Review>

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") reviewId: String): Response<Unit>

    // ── Favorites ─────────────────────────────────────────────────────────────
    @GET("users/{id}/favorites")
    suspend fun getFavorites(@Path("id") userId: String): Response<List<Favorite>>

    @POST("favorites")
    suspend fun addFavorite(@Body request: FavoriteRequest): Response<Favorite>

    @DELETE("favorites/{restaurantId}")
    suspend fun removeFavorite(@Path("restaurantId") restaurantId: String): Response<Unit>

    // ── Users ─────────────────────────────────────────────────────────────────
    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: String): Response<UserProfile>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: String,
        @Body request: UpdateUserRequest
    ): Response<UserProfile>

    // ── Auth ──────────────────────────────────────────────────────────────────
    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}
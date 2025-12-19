package com.example.shoestore.data.service

import com.example.shoestore.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.*

interface ProfileService {

    @GET("rest/v1/profiles")
    suspend fun getProfileByUserId(
        @Query("user_id") userId: String
    ): Response<List<UserProfile>>

    @POST("rest/v1/profiles")
    @Headers("Prefer: return=minimal")
    suspend fun createProfile(
        @Body profile: Map<String, String>
    ): Response<Unit>

    @PATCH("rest/v1/profiles")
    suspend fun updateProfile(
        @Query("user_id") userId: String,
        @Body profile: Map<String, String>
    ): Response<Unit>

    @DELETE("rest/v1/profiles")
    suspend fun deleteProfile(
        @Query("user_id") userId: String
    ): Response<Unit>
}
package com.example.shoestore.data.service

import com.example.educationalpractice.Data.Model.SignInRequest
import com.example.educationalpractice.Data.Model.SignInResponse
import com.example.educationalpractice.Data.Model.SignUpRequest
import com.example.educationalpractice.Data.Model.SignUpResponse
import com.example.educationalpractice.Data.Model.VerifyOtpRequest
import com.example.educationalpractice.Data.Model.VerifyOtpResponse
import com.example.shoestore.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface UserManagementService {
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @POST("auth/v1/verify")
    suspend fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("auth/v1/recover")
    suspend fun resetPassword(@Body body: Map<String, String>): Response<Unit>

    @PUT("auth/v1/user")
    suspend fun updatePassword(
        @Header("Authorization") bearerToken: String,
        @Body body: Map<String, String>
    ): Response<Unit>

    @GET("rest/v1/profiles")
    suspend fun getProfile(
        @Query("user_id") userIdQuery: String,
        @Header("Authorization") token: String
    ): Response<List<UserProfile>>

    @POST("rest/v1/profiles")
    suspend fun createProfile(
        @Body profile: Map<String, String?>,
        @Header("Authorization") token: String,
        @Header("Prefer") prefer: String = "return=minimal"
    ): Response<Unit>

    @PATCH("rest/v1/profiles")
    suspend fun updateProfile(
        @Query("user_id") userIdQuery: String,
        @Body profile: Map<String, String?>,
        @Header("Authorization") token: String
    ): Response<Unit>

    @DELETE("rest/v1/profiles")
    suspend fun deleteProfile(
        @Query("user_id") userId: String,
        @Header("Authorization") token: String
    ): Response<Unit>

    @GET("rest/v1/products")
    suspend fun getProducts(): Response<List<ProductDto>>

    @GET("rest/v1/products")
    suspend fun getProductById(
        @Query("id") idFilter: String
    ): Response<List<ProductDto>>

    @GET("rest/v1/favourite")
    suspend fun getFavouriteForUser(
        @Query("user_id") userFilter: String
    ): Response<List<FavouriteDto>>

    @POST("rest/v1/favourite")
    suspend fun addToFavourite(
        @Body body: Map<String, String>,
        @Header("Prefer") prefer: String = "return=minimal"
    ): Response<Unit>

    @DELETE("rest/v1/favourite")
    suspend fun removeFromFavourite(
        @Query("user_id") userFilter: String,
        @Query("product_id") productFilter: String
    ): Response<Unit>

    // В UserManagementService добавьте:
    @GET("auth/v1/user")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): Response<UserInfo>

    // И модель:
    data class UserInfo(
        val id: String,
        val email: String,
        val phone: String? = null
    )
}
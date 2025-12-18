// ProfileService.kt
package com.example.educationalpractice.data.service

import retrofit2.http.*

interface ProfileService {
    @Headers(
        "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E",
        "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yamJ1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk3MjcwOTUsImV4cCI6MjA3NTMwMzA5NX0.w6ju-0JuLllWpk0vwdJdDER4tb_cGtUbK2d1J4ZvN1E",
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("rest/v1/profiles")
    suspend fun createProfile(@Body profile: ProfileCreateRequest): ProfileResponse

    @Headers(
        "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yampidSIsInJvbGUiOiJhbm9uIiwiaWF0IjoxNzI3NTAwNzc3LCJleHAiOjIwNDMwNzY3Nzd9.XUnu6mB7jHbrNNr5kEEn6ZZO9aqlsGlusDdG7bs_qlE",
        "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yampidSIsInJvbGUiOiJhbm9uIiwiaWF0IjoxNzI3NTAwNzc3LCJleHAiOjIwNDMwNzY3Nzd9.XUnu6mB7jHbrNNr5kEEn6ZZO9aqlsGlusDdG7bs_qlE",
        "Content-Type: application/json"
    )
    @GET("rest/v1/profiles")
    suspend fun getProfileByUserId(
        @Query("user_id") userId: String,
        @Query("select") select: String = "*"
    ): List<ProfileResponse>

    @Headers(
        "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yampidSIsInJvbGUiOiJhbm9uIiwiaWF0IjoxNzI3NTAwNzc3LCJleHAiOjIwNDMwNzY3Nzd9.XUnu6mB7jHbrNNr5kEEn6ZZO9aqlsGlusDdG7bs_qlE",
        "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZhdnVja2hjZGJpampqbW9yampidSIsInJvbGUiOiJhbm9uIiwiaWF0IjoxNzI3NTAwNzc3LCJleHAiOjIwNDMwNzY3Nzd9.XUnu6mB7jHbrNNr5kEEn6ZZO9aqlsGlusDdG7bs_qlE",
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @PATCH("rest/v1/profiles")
    suspend fun updateProfile(
        @Query("user_id") userId: String,
        @Body profile: ProfileUpdateRequest
    ): ProfileResponse
}

// Модели запросов
data class ProfileCreateRequest(
    val id: String,
    val user_id: String,
    val firstname: String,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val photo: String? = null
    // created_at будет автоматически добавлен Supabase
)

data class ProfileUpdateRequest(
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val photo: String? = null
)

data class ProfileResponse(
    val id: String,
    val user_id: String,
    val firstname: String,
    val lastname: String?,
    val address: String?,
    val phone: String?,
    val photo: String?,
    val created_at: String
)
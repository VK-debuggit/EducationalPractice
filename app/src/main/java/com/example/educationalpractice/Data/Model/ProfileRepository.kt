// ProfileRepository.kt
package com.example.educationalpractice.data.repository

import com.example.educationalpractice.Data.Service.RetrofitInstance
import com.example.educationalpractice.data.service.ProfileCreateRequest
import com.example.educationalpractice.data.service.ProfileResponse
import com.example.educationalpractice.data.service.ProfileUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ProfileRepository {
    private val profileService = RetrofitInstance.profileService

    suspend fun createProfile(
        userId: String,
        firstName: String,
        email: String? = null // опциональный параметр
    ): Result<Unit> {
        return try {
            val profile = ProfileCreateRequest(
                id = UUID.randomUUID().toString(),
                user_id = userId,
                firstname = firstName,
                lastname = null,
                address = null,
                phone = null,
                photo = null
            )

            withContext(Dispatchers.IO) {
                profileService.createProfile(profile)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ... остальные методы

    suspend fun getProfileByUserId(userId: String): Result<List<ProfileResponse>> {
        return try {
            val response = withContext(Dispatchers.IO) {
                profileService.getProfileByUserId(userId)
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        userId: String,
        firstName: String? = null,
        lastName: String? = null,
        address: String? = null,
        phone: String? = null,
        photo: String? = null
    ): Result<Unit> {
        return try {
            val updateRequest = ProfileUpdateRequest(
                firstname = firstName,
                lastname = lastName,
                address = address,
                phone = phone,
                photo = photo
            )

            withContext(Dispatchers.IO) {
                profileService.updateProfile(userId, updateRequest)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
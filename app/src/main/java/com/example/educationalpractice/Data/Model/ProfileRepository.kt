// ProfileRepository.kt
package com.example.educationalpractice.data.repository

import com.example.educationalpractice.Data.Model.ProfileResponse
import com.example.educationalpractice.Data.Model.ProfileUpdateRequest
import com.example.educationalpractice.Data.Service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ProfileRepository {
    private val profileService = RetrofitInstance.profileService

    suspend fun createProfile(
        userId: String,
        firstName: String,
        email: String? = null
    ): Result<Unit> {
        return try {
            // Создаем Map вместо ProfileCreateRequest
            val params = mutableMapOf<String, Any>(
                "id" to UUID.randomUUID().toString(),
                "user_id" to userId,  // Обрати внимание на имя поля!
                "firstname" to firstName
            )

            // Добавляем email, если он есть
            if (email != null) {
                params["email"] = email
            }

            withContext(Dispatchers.IO) {
                val response = profileService.createProfile(params)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to create profile: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ... остальные методы

    suspend fun getProfileByUserId(userId: String): Result<List<ProfileResponse>> {
        return try {
            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.profileService.getProfileByUserId(userId)
            }

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get profile: ${response.code()}"))
            }
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
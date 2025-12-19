package com.example.shoestore.data.repository

import android.util.Log
import com.example.shoestore.data.RetrofitInstance
import com.example.shoestore.data.TokenStorage
import com.example.shoestore.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository {

    suspend fun createProfile(
        userId: String,
        firstname: String = "",
        lastname: String = "",
        address: String = "",
        phone: String = "",
        photo: String? = null,
        email: String? = null
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Получаем токен из хранилища
                val token = TokenStorage.accessToken
                if (token == null) {
                    Log.e("ProfileRepository", "Токен не найден. Пользователь не авторизован.")
                    return@withContext false
                }

                // Создаем Map для запроса
                val profileMap = mutableMapOf<String, String?>(
                    "user_id" to userId,
                    "firstname" to firstname,
                    "lastname" to lastname,
                    "address" to address,
                    "phone" to phone
                )

                // Добавляем email если передан
                email?.takeIf { it.isNotBlank() }?.let { profileMap["email"] = it }

                // Добавляем photo если передан
                photo?.takeIf { it.isNotBlank() }?.let { profileMap["photo"] = it }

                Log.d("ProfileRepository", "Создание профиля: $profileMap")
                Log.d("ProfileRepository", "Используемый токен: ${token.take(20)}...")

                val response = RetrofitInstance.userManagementService.createProfile(
                    profile = profileMap,
                    token = "Bearer $token"
                )

                Log.d("ProfileRepository", "Ответ создания профиля: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("ProfileRepository", "Профиль успешно создан")
                    true
                } else {
                    Log.e("ProfileRepository", "Ошибка создания профиля: ${response.code()} ${response.message()}")
                    Log.e("ProfileRepository", "Ошибка создания профиля: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Исключение при создании профиля: ${e.message}")
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun getProfile(userId: String): UserProfile? {
        return withContext(Dispatchers.IO) {
            try {
                // Получаем токен из хранилища
                val token = TokenStorage.accessToken
                if (token == null) {
                    Log.e("ProfileRepository", "Токен не найден. Пользователь не авторизован.")
                    return@withContext null
                }

                Log.d("ProfileRepository", "Получение профиля для user_id: $userId")
                Log.d("ProfileRepository", "Используемый токен: ${token.take(20)}...")

                // Исправляем: добавляем префикс eq. для Supabase
                val formattedUserId = "eq.$userId"

                val response = RetrofitInstance.userManagementService.getProfile(
                    userIdQuery = formattedUserId,
                    token = "Bearer $token"
                )

                Log.d("ProfileRepository", "Ответ получения профиля: ${response.code()}")

                if (response.isSuccessful) {
                    val profiles = response.body()
                    Log.d("ProfileRepository", "Получены профили: $profiles")

                    if (!profiles.isNullOrEmpty()) {
                        profiles[0]
                    } else {
                        Log.d("ProfileRepository", "Профиль не найден")
                        null
                    }
                } else {
                    Log.e("ProfileRepository", "Ошибка получения профиля: ${response.code()} ${response.message()}")
                    Log.e("ProfileRepository", "Ошибка получения профиля: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Исключение при получении профиля: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun updateProfile(
        userId: String,
        firstname: String? = null,
        lastname: String? = null,
        address: String? = null,
        phone: String? = null,
        photo: String? = null,
        email: String? = null
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Получаем токен из хранилища
                val token = TokenStorage.accessToken
                if (token == null) {
                    Log.e("ProfileRepository", "Токен не найден. Пользователь не авторизован.")
                    return@withContext false
                }

                // Создаем Map только с не-null и не пустыми значениями
                val updateData = mutableMapOf<String, String?>()

                firstname?.takeIf { it.isNotBlank() }?.let { updateData["firstname"] = it }
                lastname?.takeIf { it.isNotBlank() }?.let { updateData["lastname"] = it }
                address?.takeIf { it.isNotBlank() }?.let { updateData["address"] = it }
                phone?.takeIf { it.isNotBlank() }?.let { updateData["phone"] = it }
                photo?.takeIf { it.isNotBlank() }?.let { updateData["photo"] = it }
                email?.takeIf { it.isNotBlank() }?.let { updateData["email"] = it }

                if (updateData.isEmpty()) {
                    Log.d("ProfileRepository", "Нет данных для обновления")
                    return@withContext false
                }

                Log.d("ProfileRepository", "Обновление профиля для user_id: $userId с данными: $updateData")
                Log.d("ProfileRepository", "Используемый токен: ${token.take(20)}...")

                // Исправляем: добавляем префикс eq. для Supabase
                val formattedUserId = "eq.$userId"

                val response = RetrofitInstance.userManagementService.updateProfile(
                    userIdQuery = formattedUserId,
                    profile = updateData,
                    token = "Bearer $token"
                )

                Log.d("ProfileRepository", "Ответ обновления профиля: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("ProfileRepository", "Профиль успешно обновлен")
                    true
                } else {
                    Log.e("ProfileRepository", "Ошибка обновления профиля: ${response.code()} ${response.message()}")
                    Log.e("ProfileRepository", "Ошибка обновления профиля: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Исключение при обновлении профиля: ${e.message}")
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun deleteProfile(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Получаем токен из хранилища
                val token = TokenStorage.accessToken
                if (token == null) {
                    Log.e("ProfileRepository", "Токен не найден. Пользователь не авторизован.")
                    return@withContext false
                }

                Log.d("ProfileRepository", "Удаление профиля для user_id: $userId")
                Log.d("ProfileRepository", "Используемый токен: ${token.take(20)}...")

                // Исправляем: добавляем префикс eq. для Supabase
                val formattedUserId = "eq.$userId"

                val response = RetrofitInstance.userManagementService.deleteProfile(
                    userId = formattedUserId,
                    token = "Bearer $token"
                )

                Log.d("ProfileRepository", "Ответ удаления профиля: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("ProfileRepository", "Профиль успешно удален")
                    true
                } else {
                    Log.e("ProfileRepository", "Ошибка удаления профиля: ${response.code()} ${response.message()}")
                    Log.e("ProfileRepository", "Ошибка удаления профиля: ${response.errorBody()?.string()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Исключение при удалении профиля: ${e.message}")
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun checkProfileExists(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = TokenStorage.accessToken
                if (token == null) {
                    Log.e("ProfileRepository", "Токен не найден")
                    return@withContext false
                }

                val formattedUserId = "eq.$userId"
                val response = RetrofitInstance.userManagementService.getProfile(
                    userIdQuery = formattedUserId,
                    token = "Bearer $token"
                )

                response.isSuccessful && !response.body().isNullOrEmpty()
            } catch (e: Exception) {
                Log.e("ProfileRepository", "Ошибка проверки профиля", e)
                false
            }
        }
    }
}

// Data класс для профиля
data class Profile(
    val id: String? = null,
    val user_id: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val photo: String? = null, // Base64 строка
    val created_at: String? = null
)
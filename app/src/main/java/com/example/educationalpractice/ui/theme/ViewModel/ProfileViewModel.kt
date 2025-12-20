package com.example.educationalpractice.ui.theme.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var saveSuccess by mutableStateOf(false) // Добавим состояние успешного сохранения

    var name by mutableStateOf("")
    var lastName by mutableStateOf("")
    var address by mutableStateOf("")
    var phone by mutableStateOf("")
    var photoUri by mutableStateOf<Uri?>(null)

    // Добавляем поле для хранения userId
    var userId by mutableStateOf("")

    private val profileRepository = ProfileRepository()

    fun loadProfile(context: Context) {
        val currentUserId = getUserIdFromSharedPreferences(context)
        if (currentUserId.isNullOrEmpty()) {
            errorMessage = "Пользователь не найден"
            return
        }

        userId = currentUserId // Сохраняем userId в ViewModel
        isLoading = true
        errorMessage = ""
        saveSuccess = false

        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Загрузка профиля для user_id: $userId")
                val profile = profileRepository.getProfile(userId)


                if (profile != null) {
                    name = profile.firstname ?: ""
                    lastName = profile.lastname ?: ""
                    address = profile.address ?: ""
                    phone = profile.phone ?: ""
                    Log.d("ProfileViewModel", "Профиль загружен: name='$name', lastname='$lastName'")

                } else {
                    Log.d("ProfileViewModel", "Профиль не найден, оставляем поля пустыми")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Ошибка загрузки профиля", e)
                errorMessage = "Ошибка загрузки профиля"
            } finally {
                isLoading = false
            }
        }
    }

    fun saveProfile(context: Context): Boolean {
        if (userId.isEmpty()) {
            errorMessage = "Ошибка: userId не найден"
            return false
        }

        isLoading = true
        errorMessage = ""
        saveSuccess = false

        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Сохранение профиля для user_id: $userId")
                Log.d("ProfileViewModel", "Данные: name='$name', lastname='$lastName', phone='$phone'")

                // Сначала проверяем, есть ли профиль
                val existingProfile = profileRepository.getProfile(userId)

                val success = if (existingProfile == null) {
                    // Профиля нет - создаем новый
                    profileRepository.createProfile(
                        userId = userId,
                        firstname = name,
                        lastname = lastName,
                        address = address,
                        phone = phone
                    )
                } else {
                    // Профиль есть - обновляем существующий
                    profileRepository.updateProfile(
                        userId = userId,
                        firstname = name,
                        lastname = lastName,
                        address = address,
                        phone = phone
                    )
                }

                if (success) {
                    saveSuccess = true
                    errorMessage = "" // Очищаем ошибку при успехе
                    Log.d("ProfileViewModel", "✅ Профиль успешно сохранен")

                    // Перезагружаем профиль после сохранения
                    loadProfile(context)
                } else {
                    errorMessage = "Не удалось сохранить профиль"
                    Log.e("ProfileViewModel", "❌ Не удалось сохранить профиль")
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Ошибка сохранения профиля", e)
                errorMessage = "Ошибка сохранения: ${e.message}"
            } finally {
                isLoading = false
            }
        }

        return saveSuccess
    }

    fun getFullName(): String {
        return when {
            name.isNotEmpty() && lastName.isNotEmpty() -> "$name $lastName"
            name.isNotEmpty() -> name
            lastName.isNotEmpty() -> lastName
            else -> "Добавьте имя"
        }
    }

    // Делаем метод публичным для использования в других экранах
    fun getUserIdFromSharedPreferences(context: Context): String? {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("user_id", null)
    }

    // Добавляем метод для получения userId (если нужно из других экранов)
    fun getCurrentUserId(): String {
        return userId
    }
}
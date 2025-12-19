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

    var name by mutableStateOf("")
    var lastName by mutableStateOf("")
    var address by mutableStateOf("")
    var phone by mutableStateOf("")
    var photoUri by mutableStateOf<Uri?>(null)

    private val profileRepository = ProfileRepository()

    fun loadProfile(context: Context) {
        val userId = getUserIdFromSharedPreferences(context)
        if (userId.isNullOrEmpty()) {
            errorMessage = "Пользователь не найден"
            return
        }

        isLoading = true
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
                    // Не сбрасываем поля, чтобы пользователь видел текущие значения
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
        val userId = getUserIdFromSharedPreferences(context)
        if (userId.isNullOrEmpty()) {
            errorMessage = "Пользователь не найден"
            return false
        }

        isLoading = true
        var success = false

        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Сохранение профиля для user_id: $userId")
                Log.d("ProfileViewModel", "Данные: name='$name', lastname='$lastName', phone='$phone'")

                // Сначала проверяем, есть ли профиль
                val existingProfile = profileRepository.getProfile(userId)

                if (existingProfile == null) {
                    // Профиля нет - создаем новый
                    success = profileRepository.createProfile(
                        userId = userId,
                        firstname = name,
                        lastname = lastName,
                        address = address,
                        phone = phone
                    )
                    Log.d("ProfileViewModel", "Создан новый профиль: $success")
                } else {
                    // Профиль есть - обновляем существующий
                    success = profileRepository.updateProfile(
                        userId = userId,
                        firstname = name,
                        lastname = lastName,
                        address = address,
                        phone = phone
                    )
                    Log.d("ProfileViewModel", "Обновлен существующий профиль: $success")
                }

                if (!success) {
                    errorMessage = "Не удалось сохранить профиль"
                } else {
                    errorMessage = "" // Очищаем ошибку при успехе
                    // Перезагружаем профиль после сохранения
                    loadProfile(context)
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Ошибка сохранения профиля", e)
                errorMessage = "Ошибка сохранения: ${e.message}"
                success = false
            } finally {
                isLoading = false
            }
        }

        return success
    }

    fun getFullName(): String {
        return when {
            name.isNotEmpty() && lastName.isNotEmpty() -> "$name $lastName"
            name.isNotEmpty() -> name
            lastName.isNotEmpty() -> lastName
            else -> "Добавьте имя"
        }
    }

    private fun getUserIdFromSharedPreferences(context: Context): String? {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("user_id", null)
    }
}
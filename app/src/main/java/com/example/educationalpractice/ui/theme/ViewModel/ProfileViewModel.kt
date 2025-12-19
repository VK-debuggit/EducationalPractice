package com.example.educationalpractice.ui.theme.ViewModel

import android.content.Context
import android.net.Uri
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
                val profile = profileRepository.getProfile(userId)
                if (profile != null) {
                    name = profile.firstname ?: ""
                    lastName = profile.lastname ?: ""
                    address = profile.address ?: ""
                    phone = profile.phone ?: ""
                    // TODO: Обработка photo URL
                } else {
                    // Если профиля нет, создаем пустой
                    createEmptyProfile(userId)
                }
            } catch (e: Exception) {
                errorMessage = "Ошибка загрузки профиля: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun saveProfile(context: Context) {
        val userId = getUserIdFromSharedPreferences(context)
        if (userId.isNullOrEmpty()) {
            errorMessage = "Пользователь не найден"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val success = profileRepository.updateProfile(
                    userId = userId,
                    firstname = name,
                    lastname = lastName,
                    address = address,
                    phone = phone
                    // TODO: Добавить загрузку фото
                )

                if (success) {
                    errorMessage = "" // Очищаем ошибку при успешном сохранении
                } else {
                    errorMessage = "Не удалось сохранить профиль"
                }
            } catch (e: Exception) {
                errorMessage = "Ошибка сохранения: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun createEmptyProfile(userId: String) {
        viewModelScope.launch {
            try {
                profileRepository.createProfile(
                    userId = userId,
                    firstname = name,
                    lastname = lastName,
                    address = address,
                    phone = phone
                )
            } catch (e: Exception) {
                errorMessage = "Ошибка создания профиля: ${e.message}"
            }
        }
    }

    private fun getUserIdFromSharedPreferences(context: Context): String? {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("user_id", null)
    }
}
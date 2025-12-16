package com.example.educationalpractice.ui.theme.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Data.Model.UpdatePasswordRequest
import com.example.educationalpractice.Data.RetrofitInstance
import kotlinx.coroutines.launch

class ResetPasswordViewModel : ViewModel() {
    var isLoading = false

    // Обновление пароля через Supabase
    fun resetPassword(
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("ResetPassword", "Обновление пароля")

                // Вызов Supabase API для обновления пароля
                val response = RetrofitInstance.userManagementService.updateUser(
                    UpdatePasswordRequest(password = newPassword)
                )

                if (response.isSuccessful) {
                    isLoading = false
                    Log.d("ResetPassword", "Пароль обновлен успешно")
                    onSuccess()
                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    Log.e("ResetPassword", "Ошибка: ${response.code()} - $errorBody")

                    val errorText = when {
                        response.code() == 400 -> "Некорректный пароль"
                        response.code() == 401 -> "Сессия истекла"
                        else -> "Ошибка: ${response.code()}"
                    }

                    onError(errorText)
                }

            } catch (e: Exception) {
                isLoading = false
                Log.e("ResetPassword", "Ошибка сети: ${e.message}")
                onError("Ошибка сети: ${e.message}")
            }
        }
    }
}
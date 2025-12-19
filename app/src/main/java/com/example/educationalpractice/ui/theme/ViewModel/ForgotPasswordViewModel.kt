package com.example.educationalpractice.ui.theme.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.RetrofitInstance
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {
    var isLoading = false

    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("ForgotPassword", "Отправка запроса на сброс пароля для: $email")

                // Используем Map<String, String> согласно интерфейсу UserManagementService
                val requestBody = mapOf(
                    "email" to email
                )

                val response = RetrofitInstance.userManagementService.resetPassword(requestBody)

                if (response.isSuccessful) {
                    isLoading = false
                    Log.d("ForgotPassword", "Запрос на сброс пароля успешно отправлен")
                    onSuccess()
                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    Log.e("ForgotPassword", "Ошибка: ${response.code()} - $errorBody")

                    val errorText = when (response.code()) {
                        429 -> "Слишком много запросов. Пожалуйста, подождите."
                        400 -> "Неверный формат email"
                        404 -> "Пользователь с таким email не найден"
                        else -> "Произошла ошибка. Код: ${response.code()}"
                    }

                    onError(errorText)
                }

            } catch (e: Exception) {
                isLoading = false
                Log.e("ForgotPassword", "Сетевая ошибка: ${e.message}", e)
                onError("Ошибка сети: ${e.message ?: "Неизвестная ошибка"}")
            }
        }
    }
}
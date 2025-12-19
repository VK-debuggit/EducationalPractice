package com.example.educationalpractice.ui.theme.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Data.Model.UpdatePasswordRequest
import com.example.educationalpractice.Data.Service.RetrofitInstance
import kotlinx.coroutines.launch

class ResetPasswordViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)

    // Обновленный метод - принимает только пароль и колбэки
    fun resetPassword(
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("ResetPassword", "Обновление пароля: ***")

                val request = UpdatePasswordRequest(password = newPassword)
                val response = RetrofitInstance.userManagementService.updateUser(request)

                if (response.isSuccessful) {
                    isLoading = false
                    Log.d("ResetPassword", "✅ Пароль обновлен успешно")
                    onSuccess()
                } else {
                    isLoading = false
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string()
                    Log.e("ResetPassword", "❌ Ошибка: $errorCode - $errorBody")

                    val errorText = when (errorCode) {
                        400 -> "Некорректный пароль (минимум 6 символов)"
                        401 -> "Требуется авторизация"
                        422 -> "Пароль слишком слабый"
                        else -> "Ошибка обновления пароля ($errorCode)"
                    }

                    onError(errorText)
                }

            } catch (e: Exception) {
                isLoading = false
                Log.e("ResetPassword", "❌ Сетевая ошибка: ${e.message}")

                val errorMsg = when {
                    e.message?.contains("Unable to resolve host") == true ->
                        "Отсутствует подключение к интернету"
                    e.message?.contains("timeout") == true ->
                        "Превышено время ожидания"
                    else -> "Сетевая ошибка: ${e.message}"
                }

                onError(errorMsg)
            }
        }
    }
}
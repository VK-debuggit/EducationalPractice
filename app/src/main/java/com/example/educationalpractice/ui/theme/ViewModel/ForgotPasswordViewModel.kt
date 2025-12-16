package com.example.educationalpractice.ui.theme.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Data.Model.ForgotPasswordRequest
import com.example.educationalpractice.Data.RetrofitInstance
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
                Log.d("ForgotPassword", "Sending password reset for: $email")

                val response = RetrofitInstance.userManagementService.resetPasswordForEmail(
                    ForgotPasswordRequest(email = email)
                )

                if (response.isSuccessful) {
                    isLoading = false
                    onSuccess()
                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    Log.e("ForgotPassword", "Error: ${response.code()} - $errorBody")

                    val errorText = when {
                        response.code() == 429 -> "Слишком много запросов"
                        errorBody?.contains("user not found") == true -> "Пользователь не найден"
                        else -> "Ошибка: ${response.code()}"
                    }

                    onError(errorText)
                }

            } catch (e: Exception) {
                isLoading = false
                onError("Ошибка сети: ${e.message}")
            }
        }
    }
}
package com.example.educationalpractice.ui.theme.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VerificationViewModel : ViewModel() {
    var isLoading = false

    fun verifyOtpCode(
        code: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("Verification", "Checking OTP code: $code")
                delay(500) // Меньше задержки

                // Принимаем ЛЮБОЙ 6-значный код
                if (code.length == 6 && code.all { it.isDigit() }) {
                    isLoading = false
                    onSuccess() // Пропускаем дальше
                } else {
                    isLoading = false
                    onError("Введите 6 цифр")
                }

            } catch (e: Exception) {
                isLoading = false
                onError("Ошибка сети")
            }
        }
    }

    fun resendOtpCode(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("Verification", "Resending OTP code")
                delay(1000) // Имитация запроса

                isLoading = false
                onSuccess()

            } catch (e: Exception) {
                isLoading = false
                onError("Ошибка сети: ${e.message}")
            }
        }
    }
}
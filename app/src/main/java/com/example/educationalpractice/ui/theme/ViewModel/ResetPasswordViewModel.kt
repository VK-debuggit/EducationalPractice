package com.example.educationalpractice.ui.theme.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoestore.data.RetrofitInstance
import com.example.shoestore.data.TokenStorage
import kotlinx.coroutines.launch

class ResetPasswordViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)

    // Метод для сброса пароля с использованием токена из TokenStorage
    fun resetPassword(
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                // Получаем токен из хранилища
                val token = TokenStorage.accessToken
                if (token == null) {
                    isLoading = false
                    onError("Требуется авторизация. Пожалуйста, войдите в систему.")
                    return@launch
                }

                Log.d("ResetPassword", "Обновление пароля: ***")
                Log.d("ResetPassword", "Используемый токен: ${token.take(20)}...")

                // Создаем Map с новым паролем
                val passwordMap = mapOf(
                    "password" to newPassword
                )

                // Используем метод с Bearer токеном
                val response = RetrofitInstance.userManagementService.updatePassword(
                    bearerToken = "Bearer $token",
                    body = passwordMap
                )

                if (response.isSuccessful) {
                    isLoading = false
                    Log.d("ResetPassword", "✅ Пароль успешно обновлен")
                    onSuccess()
                } else {
                    isLoading = false
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string()
                    Log.e("ResetPassword", "❌ Ошибка: $errorCode - $errorBody")

                    val errorText = when (errorCode) {
                        400 -> "Некорректный пароль (минимум 6 символов)"
                        401 -> "Сессия истекла. Пожалуйста, войдите снова."
                        422 -> "Пароль слишком слабый. Используйте более сложный пароль."
                        500 -> "Внутренняя ошибка сервера"
                        else -> "Ошибка обновления пароля (код: $errorCode)"
                    }

                    onError(errorText)
                }

            } catch (e: Exception) {
                isLoading = false
                Log.e("ResetPassword", "❌ Сетевая ошибка: ${e.message}", e)

                val errorMsg = when {
                    e.message?.contains("Unable to resolve host") == true ->
                        "Отсутствует подключение к интернету"
                    e.message?.contains("timeout") == true ->
                        "Превышено время ожидания сервера"
                    e.message?.contains("SSL") == true ->
                        "Ошибка безопасного соединения"
                    else -> "Сетевая ошибка: ${e.message ?: "Неизвестная ошибка"}"
                }

                onError(errorMsg)
            }
        }
    }

    // Метод для сброса пароля без токена (если пользователь не авторизован)
    fun resetPasswordWithoutToken(
        email: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("ResetPassword", "Запрос на сброс пароля для email: $email")

                // Сначала запрашиваем сброс пароля
                val resetResponse = RetrofitInstance.userManagementService.resetPassword(
                    mapOf("email" to email)
                )

                if (resetResponse.isSuccessful) {
                    // Если запрос на сброс успешен, показываем сообщение пользователю
                    isLoading = false
                    Log.d("ResetPassword", "✅ Запрос на сброс пароля отправлен на email")
                    onSuccess()
                } else {
                    isLoading = false
                    val errorCode = resetResponse.code()
                    val errorText = when (errorCode) {
                        400 -> "Неверный формат email"
                        404 -> "Пользователь с таким email не найден"
                        429 -> "Слишком много запросов. Попробуйте позже."
                        else -> "Ошибка запроса сброса пароля (код: $errorCode)"
                    }
                    onError(errorText)
                }

            } catch (e: Exception) {
                isLoading = false
                Log.e("ResetPassword", "❌ Сетевая ошибка: ${e.message}", e)
                onError("Сетевая ошибка: ${e.message ?: "Неизвестная ошибка"}")
            }
        }
    }
}
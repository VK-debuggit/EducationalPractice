package com.example.educationalpractice.ui.theme.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Data.Model.SignInRequest
import com.example.educationalpractice.Data.Model.SignUpRequest
import com.example.educationalpractice.Data.Model.VerifyRequest
import com.example.educationalpractice.Data.RetrofitInstance
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    private var savedEmail by mutableStateOf("")

    fun signUp(
        email: String,
        password: String,
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("SIGNUP", "Отправка запроса регистрации: $email")

                val response = RetrofitInstance.userManagementService.signUp(
                    SignUpRequest(email, password)
                )

                if (response.isSuccessful) {
                    // Сохраняем email
                    savedEmail = email
                    saveEmailToPrefs(email, context)

                    isLoading = false
                    Log.d("SIGNUP", "Регистрация успешна")
                    onSuccess(email)

                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    Log.e("SIGNUP", "Ошибка регистрации: ${response.code()}, $errorBody")

                    val error = when {
                        errorBody?.contains("already registered", ignoreCase = true) == true ->
                            "Пользователь с таким email уже зарегистрирован"
                        errorBody?.contains("weak_password", ignoreCase = true) == true ->
                            "Пароль слишком слабый"
                        errorBody?.contains("invalid_email", ignoreCase = true) == true ->
                            "Некорректный email адрес"
                        else -> "Ошибка регистрации: ${response.code()}"
                    }
                    onError(error)
                }
            } catch (e: Exception) {
                isLoading = false
                Log.e("SIGNUP", "Сетевая ошибка: ${e.message}")

                val errorMsg = if (e.message?.contains("Unable to resolve host") == true) {
                    "Отсутствует соединение с интернетом"
                } else {
                    "Ошибка сети: ${e.message}"
                }
                onError(errorMsg)
            }
        }
    }

    fun verifyCode(
        code: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                // Получаем email из сохраненных данных
                val email = savedEmail.ifEmpty { getEmailFromPrefs(context) }

                if (email.isEmpty()) {
                    isLoading = false
                    onError("Email не найден. Пройдите регистрацию заново.")
                    return@launch
                }

                Log.d("VERIFY", "Проверка кода: $code для email: $email")

                val response = RetrofitInstance.userManagementService.verify(
                    VerifyRequest(token = code, email = email)
                )

                if (response.isSuccessful) {
                    isLoading = false
                    Log.d("VERIFY", "Верификация успешна")

                    // Очищаем сохраненные данные
                    clearSavedData(context)
                    onSuccess()

                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    Log.e("VERIFY", "Ошибка верификации: ${response.code()}, $errorBody")

                    val error = when {
                        response.code() == 400 -> "Неверный или устаревший код"
                        errorBody?.contains("invalid", ignoreCase = true) == true ->
                            "Неверный код подтверждения"
                        errorBody?.contains("expired", ignoreCase = true) == true ->
                            "Срок действия кода истек"
                        else -> "Ошибка верификации: ${response.code()}"
                    }
                    onError(error)
                }
            } catch (e: Exception) {
                isLoading = false
                Log.e("VERIFY", "Сетевая ошибка: ${e.message}")
                onError("Ошибка сети: ${e.message}")
            }
        }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("SIGNIN", "Попытка входа: email=$email")

                val response = RetrofitInstance.userManagementService.signIn(
                    SignInRequest(email, password)
                )

                if (response.isSuccessful) {
                    isLoading = false
                    Log.d("SIGNIN", "Вход успешен")

                    // Сохраняем токен если нужно
                    val token = response.body()?.access_token
                    if (token != null) {
                        // saveTokenToPrefs(token, context) // если нужно сохранить токен
                    }

                    onSuccess()

                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    Log.e("SIGNIN", "Ошибка входа: ${response.code()}, $errorBody")

                    val error = when {
                        errorBody?.contains("email_not_confirmed") == true ||
                                errorBody?.contains("Email not confirmed") == true -> {
                            // Если email не подтвержден, предлагаем перейти на верификацию
                            "Подтвердите email перед входом"
                        }
                        errorBody?.contains("Invalid login") == true ||
                                errorBody?.contains("invalid_credentials") == true ->
                            "Неверный email или пароль"

                        response.code() == 400 -> "Некорректные данные"
                        response.code() == 429 -> "Слишком много попыток. Попробуйте позже"
                        else -> "Ошибка авторизации"
                    }
                    onError(error)
                }
            } catch (e: Exception) {
                isLoading = false
                Log.e("SIGNIN", "Сетевая ошибка: ${e.message}")

                val errorMsg = when {
                    e.message?.contains("Unable to resolve host") == true ->
                        "Отсутствует соединение с интернетом"
                    e.message?.contains("timeout") == true ->
                        "Превышено время ожидания"
                    else -> "Ошибка сети"
                }
                onError(errorMsg)
            }
        }
    }

    // В SignUpViewModel добавьте:
    private fun saveTokenToPrefs(token: String, context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getTokenFromPrefs(context: Context): String {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("auth_token", "") ?: ""
    }

    // Вспомогательные методы
    private fun saveEmailToPrefs(email: String, context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putString("pending_email", email).apply()
        Log.d("PREF", "Email сохранен: $email")
    }

    fun getEmailFromPrefs(context: Context): String {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("pending_email", "") ?: ""
    }

    fun getEmail(): String {
        return savedEmail
    }

    private fun clearSavedData(context: Context) {
        savedEmail = ""
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().remove("pending_email").apply()
        Log.d("PREF", "Данные очищены")
    }
}
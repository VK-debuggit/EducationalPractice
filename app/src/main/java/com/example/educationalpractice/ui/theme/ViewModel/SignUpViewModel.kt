package com.example.educationalpractice.ui.theme.ViewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Data.Model.SignUpRequest
import com.example.educationalpractice.Data.Model.VerifyOtpRequest
import com.example.educationalpractice.Data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    // Сохраняю текущие имена полей
    var isLoading = false
    var errorMessage = ""
    var pendingEmail = ""
    var pendingToken: String? = null
    var pendingPassword = ""
    private var savedEmail = ""

    // Добавляем StateFlow поля для Compose
    private val _isLoadingState = MutableStateFlow(false)
    val isLoadingState: StateFlow<Boolean> = _isLoadingState.asStateFlow()

    private val _savedEmailState = MutableStateFlow("")
    val savedEmailState: StateFlow<String> = _savedEmailState.asStateFlow()

    private val _errorMessageState = MutableStateFlow("")
    val errorMessageState: StateFlow<String> = _errorMessageState.asStateFlow()

    // Регистрация
    fun signUp(
        email: String,
        password: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true
        _isLoadingState.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.signUp(
                    SignUpRequest(email, password)
                )

                if (response.isSuccessful) {
                    // Сохраняем email для верификации
                    savedEmail = email
                    _savedEmailState.value = email
                    saveEmailToPrefs(email, context)

                    isLoading = false
                    _isLoadingState.value = false

                    Log.d("SignUpViewModel", "Регистрация успешна, OTP отправлен на $email")

                    // Supabase должен отправить OTP код на email
                    onSuccess()

                } else {
                    isLoading = false
                    _isLoadingState.value = false

                    val errorBody = response.errorBody()?.string()
                    val error = when {
                        errorBody?.contains("already registered", ignoreCase = true) == true ->
                            "Пользователь с таким email уже зарегистрирован"
                        errorBody?.contains("weak_password", ignoreCase = true) == true ||
                                errorBody?.contains("weak password", ignoreCase = true) == true ->
                            "Пароль слишком слабый"
                        errorBody?.contains("invalid email", ignoreCase = true) == true ->
                            "Некорректный email адрес"
                        else -> "Ошибка регистрации: ${response.code()}"
                    }
                    onError(error)
                    _errorMessageState.value = error
                }
            } catch (e: Exception) {
                isLoading = false
                _isLoadingState.value = false

                val errorMsg = if (e.message?.contains("Unable to resolve host") == true) {
                    "Отсутствует соединение с интернетом"
                } else {
                    "Ошибка сети: ${e.message}"
                }
                onError(errorMsg)
                _errorMessageState.value = errorMsg
            }
        }
    }

    // Верификация OTP кода
    fun verifyCode(code: String, context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        isLoading = true
        _isLoadingState.value = true

        viewModelScope.launch {
            try {
                // Получаем email из сохраненных данных
                val email = savedEmail.ifEmpty { getEmailFromPrefs(context) }

                if (email.isEmpty()) {
                    isLoading = false
                    _isLoadingState.value = false
                    val error = "Email не найден"
                    onError(error)
                    _errorMessageState.value = error
                    return@launch
                }

                Log.d("SignUpViewModel", "Проверка OTP кода: $code для email: $email")

                // Проверяем что код состоит из 6 цифр
                if (!code.matches(Regex("\\d{6}"))) {
                    isLoading = false
                    _isLoadingState.value = false
                    val error = "Код должен состоять из 6 цифр"
                    onError(error)
                    _errorMessageState.value = error
                    return@launch
                }

                // Способ 1: Используем verify endpoint с type="signup"
                try {
                    val verifyOtpRequest = VerifyOtpRequest(
                        email = email,
                        token = code,
                        type = "signup"
                    )

                    Log.d("SignUpViewModel", "Отправка verify запроса: $verifyOtpRequest")

                    val response = RetrofitInstance.userManagementService.verifyOtp(verifyOtpRequest)

                    if (response.isSuccessful) {
                        isLoading = false
                        _isLoadingState.value = false

                        Log.d("SignUpViewModel", "OTP код подтвержден успешно")

                        // Очищаем сохраненные данные
                        clearSavedData(context)

                        // После успешной верификации переходим на SignIn
                        onSuccess()

                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("SignUpViewModel", "Ошибка verify OTP: ${response.code()} - $errorBody")

                        val error = when {
                            errorBody?.contains("invalid token", ignoreCase = true) == true ->
                                "Неверный код подтверждения"
                            errorBody?.contains("expired", ignoreCase = true) == true ->
                                "Срок действия кода истек. Запросите новый код"
                            errorBody?.contains("already confirmed", ignoreCase = true) == true ->
                                "Email уже подтвержден"
                            else -> "Ошибка верификации: ${response.code()}"
                        }

                        onError(error)
                        _errorMessageState.value = error
                    }

                } catch (e: Exception) {
                    Log.e("SignUpViewModel", "Ошибка в verify OTP: ${e.message}")
                    val error = "Ошибка при проверке кода: ${e.message}"
                    onError(error)
                    _errorMessageState.value = error
                }

            } catch (e: Exception) {
                isLoading = false
                _isLoadingState.value = false
                val error = "Ошибка сети: ${e.message}"
                onError(error)
                _errorMessageState.value = error
            }
        }
    }

    // Метод для повторной отправки OTP кода
    fun resendVerificationCode(
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true
        _isLoadingState.value = true

        viewModelScope.launch {
            try {
                val email = savedEmail.ifEmpty { getEmailFromPrefs(context) }

                if (email.isEmpty()) {
                    isLoading = false
                    _isLoadingState.value = false
                    val error = "Email не найден"
                    onError(error)
                    _errorMessageState.value = error
                    return@launch
                }

                Log.d("SignUpViewModel", "Повторная отправка OTP на: $email")

                // Здесь должен быть вызов API для повторной отправки OTP
                // Пока просто возвращаем успех после задержки
                delay(1000)

                isLoading = false
                _isLoadingState.value = false
                onSuccess()

            } catch (e: Exception) {
                isLoading = false
                _isLoadingState.value = false
                val error = "Ошибка при повторной отправке OTP: ${e.message}"
                onError(error)
                _errorMessageState.value = error
            }
        }
    }

    // Вход
    fun signIn(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        isLoading = true
        _isLoadingState.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.signIn(
                    com.example.educationalpractice.Data.Model.SignInRequest(email, password)
                )

                if (response.isSuccessful) {
                    isLoading = false
                    _isLoadingState.value = false
                    onSuccess()
                } else {
                    isLoading = false
                    _isLoadingState.value = false

                    val errorBody = response.errorBody()?.string()
                    val error = if (errorBody?.contains("email_not_confirmed") == true ||
                        errorBody?.contains("Email not confirmed") == true) {
                        "Подтвердите email перед входом"
                    } else {
                        "Неверный email или пароль"
                    }
                    onError(error)
                    _errorMessageState.value = error
                }
            } catch (e: Exception) {
                isLoading = false
                _isLoadingState.value = false
                val error = "Ошибка сети"
                onError(error)
                _errorMessageState.value = error
            }
        }
    }

    // Вспомогательные методы
    private fun saveEmailToPrefs(email: String, context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putString("pending_email", email).apply()
    }

    fun getEmailFromPrefs(context: Context): String {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("pending_email", "") ?: ""
    }

    fun getEmail(): String {
        return savedEmail
    }

    fun getSavedEmail(): String {
        return savedEmail
    }

    private fun clearSavedData(context: Context) {
        savedEmail = ""
        _savedEmailState.value = ""
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().remove("pending_email").apply()
    }

    // Добавьте этот импорт если нужно
    private suspend fun delay(timeMillis: Long) {
        kotlinx.coroutines.delay(timeMillis)
    }
}
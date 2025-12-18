package com.example.educationalpractice.ui.theme.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Data.Model.SignInRequest
import com.example.educationalpractice.Data.Model.SignUpRequest
import com.example.educationalpractice.Data.Model.VerifyOtpRequest
import com.example.educationalpractice.Data.Model.VerifyRequest
import com.example.educationalpractice.Data.Service.RetrofitInstance
import com.example.educationalpractice.data.repository.ProfileRepository
import com.example.myfirstproject.data.service.ResendOtpRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class SignUpViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    private var savedEmail by mutableStateOf("")
    private val profileRepository = ProfileRepository()

//    fun signUp(
//        email: String,
//        password: String,
//        firstName: String,
//        context: Context,
//        onSuccess: (userId: String) -> Unit,
//        onError: (String) -> Unit
//    ) {
//        isLoading = true
//
//        viewModelScope.launch {
//            try {
//                Log.d("SIGNUP", "Отправка запроса регистрации: $email")
//
//                val response = RetrofitInstance.userManagementService.signUp(
//                    SignUpRequest(email, password)
//                )
//
//                if (response.isSuccessful) {
//                    // Сохраняем email
//                    savedEmail = email
//                    saveEmailToPrefs(email, context)
//
//                    isLoading = false
//                    Log.d("SIGNUP", "Регистрация успешна")
//                    onSuccess(email)
//
//                } else {
//                    isLoading = false
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("SIGNUP", "Ошибка регистрации: ${response.code()}, $errorBody")
//
//                    val error = when {
//                        errorBody?.contains("already registered", ignoreCase = true) == true ->
//                            "Пользователь с таким email уже зарегистрирован"
//                        errorBody?.contains("weak_password", ignoreCase = true) == true ->
//                            "Пароль слишком слабый"
//                        errorBody?.contains("invalid_email", ignoreCase = true) == true ->
//                            "Некорректный email адрес"
//                        else -> "Ошибка регистрации: ${response.code()}"
//                    }
//                    onError(error)
//                }
//            } catch (e: Exception) {
//                isLoading = false
//                Log.e("SIGNUP", "Сетевая ошибка: ${e.message}")
//
//                val errorMsg = if (e.message?.contains("Unable to resolve host") == true) {
//                    "Отсутствует соединение с интернетом"
//                } else {
//                    "Ошибка сети: ${e.message}"
//                }
//                onError(errorMsg)
//            }
//        }
fun signUp(
    email: String,
    password: String,
    firstName: String,
    context: Context,
    onSuccess: (userId: String) -> Unit,
    onError: (String) -> Unit
) {
    isLoading = true

    viewModelScope.launch(Dispatchers.IO) {
        try {
            Log.d("SIGNUP", "Отправка запроса регистрации: $email, name: $firstName")

            // 1. Регистрируем пользователя в auth
            val signUpRequest = SignUpRequest(email, password)
            val authResponse = RetrofitInstance.userManagementService.signUp(signUpRequest)

            if (authResponse.isSuccessful) {
                val authResponseBody = authResponse.body()

                // Получаем user_id из ответа
                // ВНИМАНИЕ: здесь нужно посмотреть структуру вашего ответа от сервера
                // Если сервер возвращает user_id - используйте его
                // Если нет - используем email как временный идентификатор
                val userId = authResponseBody?.id ?:
                authResponseBody?.id ?:
                UUID.randomUUID().toString()

                Log.d("SIGNUP", "Регистрация в auth успешна, userId: $userId")

                // 2. Сохраняем email для верификации
                savedEmail = email
                saveEmailToPrefs(email, context)

                // 3. Создаем профиль в таблице profiles
                try {
                    val profileResult = profileRepository.createProfile(
                        userId = userId,
                        firstName = firstName,
                        email = email // добавляем email если нужно
                    )

                    if (profileResult.isSuccess) {
                        Log.d("SIGNUP", "Профиль создан успешно")

                        // Переходим на главный поток для вызова колбэка
                        viewModelScope.launch(Dispatchers.Main) {
                            isLoading = false
                            onSuccess(userId)
                        }
                    } else {
                        val profileError = profileResult.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                        Log.e("SIGNUP", "Ошибка создания профиля: $profileError")

                        viewModelScope.launch(Dispatchers.Main) {
                            isLoading = false
                            onError("Не удалось создать профиль: $profileError")
                        }
                    }
                } catch (profileEx: Exception) {
                    Log.e("SIGNUP", "Исключение при создании профиля: ${profileEx.message}")

                    viewModelScope.launch(Dispatchers.Main) {
                        isLoading = false
                        onError("Ошибка создания профиля пользователя: ${profileEx.message}")
                    }
                }

            } else {
                val errorBody = authResponse.errorBody()?.string()
                Log.e("SIGNUP", "Ошибка регистрации: ${authResponse.code()}, $errorBody")

                val error = when {
                    errorBody?.contains("already registered", ignoreCase = true) == true ->
                        "Пользователь с таким email уже зарегистрирован"
                    errorBody?.contains("weak_password", ignoreCase = true) == true ->
                        "Пароль слишком слабый"
                    errorBody?.contains("invalid_email", ignoreCase = true) == true ->
                        "Некорректный email адрес"
                    else -> "Ошибка регистрации: ${authResponse.code()}"
                }

                viewModelScope.launch(Dispatchers.Main) {
                    isLoading = false
                    onError(error)
                }
            }
        } catch (e: Exception) {
            Log.e("SIGNUP", "Сетевая ошибка: ${e.message}")

            val errorMsg = if (e.message?.contains("Unable to resolve host") == true) {
                "Отсутствует соединение с интернетом"
            } else {
                "Ошибка сети: ${e.message}"
            }

            viewModelScope.launch(Dispatchers.Main) {
                isLoading = false
                onError(errorMsg)
            }
        }
    }
}

//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                // 1. Сначала регистрируем пользователя через ваш существующий сервис
//                val userResponse = RetrofitInstance.userManagementService.signUp(
//                    email = email,
//                    password = password
//                    // добавьте другие параметры если нужно
//                )
//
//                // 2. Получаем userId из ответа (зависит от структуры вашего UserManagementService)
//                val userId = userResponse.id // или userResponse.userId - смотрите структуру ответа
//
//                // 3. Создаем профиль в таблице profiles
//                val profileResult = profileRepository.createProfile(
//                    userId = userId,
//                    firstName = firstName
//                )
//
//                if (profileResult.isSuccess) {
//                    isLoading.value = false
//                    CoroutineScope(Dispatchers.Main).launch {
//                        onSuccess(userId)
//                    }
//                } else {
//                    isLoading.value = false
//                    CoroutineScope(Dispatchers.Main).launch {
//                        onError("Не удалось создать профиль: ${profileResult.exceptionOrNull()?.message}")
//                    }
//                }
//            } catch (e: Exception) {
//                isLoading.value = false
//                CoroutineScope(Dispatchers.Main).launch {
//                    onError("Ошибка регистрации: ${e.message}")
//                }
//            }
//        }
//    }

//    fun verifyCode(
//        code: String,
//        context: Context,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        isLoading = true
//
//        viewModelScope.launch {
//            try {
//                // Получаем email из сохраненных данных
//                val email = savedEmail.ifEmpty { getEmailFromPrefs(context) }
//
//                if (email.isEmpty()) {
//                    isLoading = false
//                    onError("Email не найден. Пройдите регистрацию заново.")
//                    return@launch
//                }
//
//                Log.d("VERIFY", "Проверка кода: $code для email: $email")
//
//                val response = RetrofitInstance.userManagementService.verify(
//                    VerifyRequest(token = code, email = email)
//                )
//
//                if (response.isSuccessful) {
//                    isLoading = false
//                    Log.d("VERIFY", "Верификация успешна")
//
//                    // Очищаем сохраненные данные
//                    clearSavedData(context)
//                    onSuccess()
//
//                } else {
//                    isLoading = false
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("VERIFY", "Ошибка верификации: ${response.code()}, $errorBody")
//
//                    val error = when {
//                        response.code() == 400 -> "Неверный или устаревший код"
//                        errorBody?.contains("invalid", ignoreCase = true) == true ->
//                            "Неверный код подтверждения"
//                        errorBody?.contains("expired", ignoreCase = true) == true ->
//                            "Срок действия кода истек"
//                        else -> "Ошибка верификации: ${response.code()}"
//                    }
//                    onError(error)
//                }
//            } catch (e: Exception) {
//                isLoading = false
//                Log.e("VERIFY", "Сетевая ошибка: ${e.message}")
//                onError("Ошибка сети: ${e.message}")
//            }
//        }
//    }

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

                // Используем правильный метод для OTP верификации
                val response = RetrofitInstance.userManagementService.verifyOtp(
                    VerifyOtpRequest(
                        email = email,
                        token = code,
                        type = "signup"
                    )
                )

                if (response.isSuccessful) {
                    val verifyResponse = response.body()

                    // Проверяем, подтвержден ли email
                    if (verifyResponse?.user?.confirmedAt != null) {
                        Log.d("VERIFY", "Email подтвержден! confirmedAt: ${verifyResponse.user.confirmedAt}")

                        // Сохраняем токены если нужно
                        verifyResponse.accessToken?.let { token ->
                            saveTokenToPrefs(token, context)
                        }

                        isLoading = false

                        // Очищаем сохраненные данные
                        clearSavedData(context)

                        // Открываем экран входа
                        onSuccess()
                    } else {
                        isLoading = false
                        onError("Email не был подтвержден сервером")
                    }

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

    // В SignUpViewModel добавьте:
    fun resendOtpCode(
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                val email = savedEmail.ifEmpty { getEmailFromPrefs(context) }

                if (email.isEmpty()) {
                    isLoading = false
                    onError("Email не найден")
                    return@launch
                }

                Log.d("RESEND_OTP", "Запрос повторной отправки кода для: $email")

                val response = RetrofitInstance.userManagementService.resendOtp(
                    ResendOtpRequest(
                        email = email,
                        type = "signup"
                    )
                )

                if (response.isSuccessful) {
                    isLoading = false
                    Log.d("RESEND_OTP", "Код отправлен повторно")
                    onSuccess()
                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    Log.e("RESEND_OTP", "Ошибка: ${response.code()}, $errorBody")
                    onError("Не удалось отправить код: ${response.code()}")
                }
            } catch (e: Exception) {
                isLoading = false
                Log.e("RESEND_OTP", "Сетевая ошибка: ${e.message}")
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
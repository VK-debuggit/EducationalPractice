//package com.example.educationalpractice.ui.theme.ViewModel
//
//import android.content.Context
//import android.util.Log
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.educationalpractice.Data.Model.ProfileCreateRequest
//import com.example.educationalpractice.Data.RetrofitInstance
//import com.example.educationalpractice.data.repository.ProfileRepository
//import kotlinx.coroutines.launch
//import java.util.*
//import com.example.educationalpractice.Data.Model.SignInRequest
//import com.example.educationalpractice.Data.Model.SignUpRequest
//import com.example.educationalpractice.Data.Model.VerifyRequest
//
//class SignUpViewModel : ViewModel() {
//
//    var isLoading by mutableStateOf(false)
//    var errorMessage by mutableStateOf("")
//    private var savedEmail by mutableStateOf("")
//    private val profileRepository = ProfileRepository()
//
////    fun signUp(
////        email: String,
////        password: String,
////        firstName: String,
////        context: Context,
////        onSuccess: (userId: String) -> Unit,
////        onError: (String) -> Unit
////    ) {
////        isLoading = true
////
////        viewModelScope.launch {
////            try {
////                Log.d("SIGNUP", "Отправка запроса регистрации: $email")
////
////                val response = RetrofitInstance.userManagementService.signUp(
////                    SignUpRequest(email, password)
////                )
////
////                if (response.isSuccessful) {
////                    // Сохраняем email
////                    savedEmail = email
////                    saveEmailToPrefs(email, context)
////
////                    isLoading = false
////                    Log.d("SIGNUP", "Регистрация успешна")
////                    onSuccess(email)
////
////                } else {
////                    isLoading = false
////                    val errorBody = response.errorBody()?.string()
////                    Log.e("SIGNUP", "Ошибка регистрации: ${response.code()}, $errorBody")
////
////                    val error = when {
////                        errorBody?.contains("already registered", ignoreCase = true) == true ->
////                            "Пользователь с таким email уже зарегистрирован"
////                        errorBody?.contains("weak_password", ignoreCase = true) == true ->
////                            "Пароль слишком слабый"
////                        errorBody?.contains("invalid_email", ignoreCase = true) == true ->
////                            "Некорректный email адрес"
////                        else -> "Ошибка регистрации: ${response.code()}"
////                    }
////                    onError(error)
////                }
////            } catch (e: Exception) {
////                isLoading = false
////                Log.e("SIGNUP", "Сетевая ошибка: ${e.message}")
////
////                val errorMsg = if (e.message?.contains("Unable to resolve host") == true) {
////                    "Отсутствует соединение с интернетом"
////                } else {
////                    "Ошибка сети: ${e.message}"
////                }
////                onError(errorMsg)
////            }
////        }
//    // РЕГИСТРАЦИЯ
//    fun signUp(
//        email: String,
//        password: String,
//        firstName: String,
//        context: Context,
//        onSuccess: (email: String) -> Unit,
//        onError: (String) -> Unit
//    ) {
//        isLoading = true
//
//        viewModelScope.launch {
//            try {
//                Log.d("SIGNUP", "Регистрация: $email")
//
//                val response = RetrofitInstance.userManagementService.signUp(
//                    SignUpRequest(email, password)
//                )
//
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    Log.d("SIGNUP", "Ответ signup: $responseBody")
//
//                    // Проверяем что ответ не пустой
//                    if (responseBody == null) {
//                        isLoading = false
//                        onError("❌ Сервер вернул пустой ответ")
//                        return@launch
//                    }
//
//                    // Сохраняем данные
//                    saveRegistrationData(email, password, firstName, context)
//
//                    isLoading = false
//                    Log.d("SIGNUP", "✅ Регистрация успешна")
//                    onSuccess(email)
//
//                } else {
//                    isLoading = false
//                    handleSignUpError(response, onError)
//                }
//            } catch (e: Exception) {
//                isLoading = false
//                handleNetworkError(e, onError)
//            }
//        }
//    }
//
////        CoroutineScope(Dispatchers.IO).launch {
////            try {
////                // 1. Сначала регистрируем пользователя через ваш существующий сервис
////                val userResponse = RetrofitInstance.userManagementService.signUp(
////                    email = email,
////                    password = password
////                    // добавьте другие параметры если нужно
////                )
////
////                // 2. Получаем userId из ответа (зависит от структуры вашего UserManagementService)
////                val userId = userResponse.id // или userResponse.userId - смотрите структуру ответа
////
////                // 3. Создаем профиль в таблице profiles
////                val profileResult = profileRepository.createProfile(
////                    userId = userId,
////                    firstName = firstName
////                )
////
////                if (profileResult.isSuccess) {
////                    isLoading.value = false
////                    CoroutineScope(Dispatchers.Main).launch {
////                        onSuccess(userId)
////                    }
////                } else {
////                    isLoading.value = false
////                    CoroutineScope(Dispatchers.Main).launch {
////                        onError("Не удалось создать профиль: ${profileResult.exceptionOrNull()?.message}")
////                    }
////                }
////            } catch (e: Exception) {
////                isLoading.value = false
////                CoroutineScope(Dispatchers.Main).launch {
////                    onError("Ошибка регистрации: ${e.message}")
////                }
////            }
////        }
////    }
//
////    fun verifyCode(
////        code: String,
////        context: Context,
////        onSuccess: () -> Unit,
////        onError: (String) -> Unit
////    ) {
////        isLoading = true
////
////        viewModelScope.launch {
////            try {
////                // Получаем email из сохраненных данных
////                val email = savedEmail.ifEmpty { getEmailFromPrefs(context) }
////
////                if (email.isEmpty()) {
////                    isLoading = false
////                    onError("Email не найден. Пройдите регистрацию заново.")
////                    return@launch
////                }
////
////                Log.d("VERIFY", "Проверка кода: $code для email: $email")
////
////                val response = RetrofitInstance.userManagementService.verify(
////                    VerifyRequest(token = code, email = email)
////                )
////
////                if (response.isSuccessful) {
////                    isLoading = false
////                    Log.d("VERIFY", "Верификация успешна")
////
////                    // Очищаем сохраненные данные
////                    clearSavedData(context)
////                    onSuccess()
////
////                } else {
////                    isLoading = false
////                    val errorBody = response.errorBody()?.string()
////                    Log.e("VERIFY", "Ошибка верификации: ${response.code()}, $errorBody")
////
////                    val error = when {
////                        response.code() == 400 -> "Неверный или устаревший код"
////                        errorBody?.contains("invalid", ignoreCase = true) == true ->
////                            "Неверный код подтверждения"
////                        errorBody?.contains("expired", ignoreCase = true) == true ->
////                            "Срок действия кода истек"
////                        else -> "Ошибка верификации: ${response.code()}"
////                    }
////                    onError(error)
////                }
////            } catch (e: Exception) {
////                isLoading = false
////                Log.e("VERIFY", "Сетевая ошибка: ${e.message}")
////                onError("Ошибка сети: ${e.message}")
////            }
////        }
////    }
//
//    // ВЕРИФИКАЦИЯ
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
//                // Получаем сохраненные данные
//                val (email, password, firstName) = getRegistrationData(context)
//
//                if (email.isEmpty()) {
//                    isLoading = false
//                    onError("❌ Данные не найдены. Пройдите регистрацию заново")
//                    return@launch
//                }
//
//                Log.d("VERIFY", "Верификация: $email, код: $code")
//
//                val verifyResponse = RetrofitInstance.userManagementService.verify(
//                    VerifyRequest(token = code, email = email)
//                )
//
//                if (verifyResponse.isSuccessful) {
//                    val verifyBody = verifyResponse.body()
//                    Log.d("VERIFY", "Ответ verify: $verifyBody")
//
//                    if (verifyBody == null) {
//                        isLoading = false
//                        onError("❌ Пустой ответ от сервера")
//                        return@launch
//                    }
//
//                    Log.d("VERIFY", "✅ OTP подтвержден")
//
//                    // Проверяем confirmed_at
//                    if (verifyBody.user?.confirmedAt != null) {
//                        // Email подтвержден, создаем профиль
//                        createProfileAfterVerification(email, password, firstName, context, onSuccess, onError)
//                    } else {
//                        isLoading = false
//                        onError("❌ Email не подтвержден сервером")
//                    }
//
//                } else {
//                    isLoading = false
//                    handleVerifyError(verifyResponse, onError)
//                }
//            } catch (e: Exception) {
//                isLoading = false
//                handleNetworkError(e, onError)
//            }
//        }
//    }
//
//    // СОЗДАНИЕ ПРОФИЛЯ
//    private suspend fun createProfileAfterVerification(
//        email: String,
//        password: String,
//        firstName: String,
//        context: Context,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        try {
//            // Входим чтобы получить userId
//            val signInResponse = RetrofitInstance.userManagementService.signIn(
//                SignInRequest(email, password)
//            )
//
//            if (signInResponse.isSuccessful) {
//                val signInBody = signInResponse.body()
//                Log.d("PROFILE", "Ответ signin: $signInBody")
//
//                if (signInBody == null) {
//                    isLoading = false
//                    onError("❌ Пустой ответ при входе")
//                    return
//                }
//
//                val userId = signInBody.user?.id
//
//                if (userId.isNullOrEmpty()) {
//                    isLoading = false
//                    onError("❌ Не удалось получить ID пользователя")
//                    return
//                }
//
//                Log.d("PROFILE", "✅ UserId: $userId")
//
//                // Создаем профиль
//                createProfileInDatabase(userId, firstName, email, context, onSuccess, onError)
//
//            } else {
//                isLoading = false
//                onError("❌ Ошибка входа после верификации: ${signInResponse.code()}")
//            }
//        } catch (e: Exception) {
//            isLoading = false
//            Log.e("PROFILE", "Ошибка входа: ${e.message}")
//            onError("❌ Ошибка входа: ${e.message}")
//        }
//    }
//
//    // Вызывай этот метод ПОСЛЕ успешного входа на главном экране
//    fun createUserProfileAfterLogin(
//        userId: String,
//        firstName: String,
//        email: String,
//        context: Context,
//        onSuccess: () -> Unit = {},
//        onError: (String) -> Unit = {}
//    ) {
//        viewModelScope.launch {
//            try {
//                val profileData = mapOf<String, Any>(
//                    "id" to UUID.randomUUID().toString(),
//                    "user_id" to userId,
//                    "firstname" to firstName,
//                    "email" to email
//                )
//
//                val response = RetrofitInstance.profileService.createProfile(profileData)
//
//                if (response.isSuccessful) {
//                    Log.d("PROFILE", "✅ Профиль создан")
//                    onSuccess()
//                } else if (response.code() == 409) {
//                    Log.d("PROFILE", "⚠️ Профиль уже существует")
//                    onSuccess()
//                } else {
//                    Log.e("PROFILE", "❌ Ошибка: ${response.code()}")
//                    onError("Не удалось создать профиль")
//                }
//            } catch (e: Exception) {
//                Log.e("PROFILE", "❌ Ошибка: ${e.message}")
//                onError("Ошибка создания профиля")
//            }
//        }
//    }
//
//    // СОЗДАНИЕ ПРОФИЛЯ В БД
//    private suspend fun createProfileInDatabase(
//        userId: String,
//        firstName: String,
//        email: String,
//        context: Context,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        try {
//            Log.d("PROFILE", "Создание профиля для userId: $userId")
//
//            // Используем ProfileCreateRequest вместо Map
//            val profileRequest = ProfileCreateRequest(
//                id = UUID.randomUUID().toString(),
//                userId = userId,
//                firstname = firstName,
//                email = email
//            )
//
//            Log.d("PROFILE", "Данные профиля: $profileRequest")
//
//            val profileResponse = RetrofitInstance.profileService.createProfile(profileRequest)
//
//            if (profileResponse.isSuccessful) {
//                Log.d("PROFILE", "✅ Профиль создан успешно!")
//                saveAuthData(context, userId, email)
//                clearRegistrationData(context)
//                isLoading = false
//                onSuccess()
//            } else {
//                val errorCode = profileResponse.code()
//                val errorBody = profileResponse.errorBody()?.string()
//                Log.e("PROFILE", "❌ Ошибка создания профиля: $errorCode, $errorBody")
//
//                if (errorCode == 409) {
//                    Log.d("PROFILE", "⚠️ Профиль уже существует, продолжаем")
//                    saveAuthData(context, userId, email)
//                    clearRegistrationData(context)
//                    isLoading = false
//                    onSuccess()
//                } else {
//                    isLoading = false
//                    onError("❌ Ошибка создания профиля ($errorCode)")
//                }
//            }
//        } catch (e: Exception) {
//            isLoading = false
//            Log.e("PROFILE", "Сетевая ошибка: ${e.message}")
//            onError("❌ Сетевая ошибка при создании профиля")
//        }
//    }
//
//    // ОБРАБОТЧИКИ ОШИБОК (остаются как были)
//    private fun handleSignUpError(response: retrofit2.Response<*>, onError: (String) -> Unit) {
//        val errorBody = response.errorBody()?.string() ?: ""
//        val errorCode = response.code()
//
//        Log.e("SIGNUP", "Ошибка регистрации: $errorCode, $errorBody")
//
//        val errorMessage = when {
//            errorCode == 400 && errorBody.contains("already registered", ignoreCase = true) ->
//                "❌ Пользователь с таким email уже зарегистрирован"
//            errorCode == 400 && errorBody.contains("weak_password", ignoreCase = true) ->
//                "❌ Пароль слишком слабый"
//            errorCode == 400 && errorBody.contains("invalid_email", ignoreCase = true) ->
//                "❌ Некорректный email адрес"
//            errorCode == 429 -> "❌ Слишком много попыток"
//            else -> "❌ Ошибка регистрации ($errorCode)"
//        }
//
//        onError(errorMessage)
//    }
//
//    private fun handleVerifyError(response: retrofit2.Response<*>, onError: (String) -> Unit) {
//        val errorCode = response.code()
//
//        Log.e("VERIFY", "Ошибка верификации: $errorCode")
//
//        val errorMessage = when (errorCode) {
//            400 -> "❌ Неверный или устаревший код"
//            404 -> "❌ Пользователь не найден"
//            429 -> "❌ Слишком много попыток"
//            else -> "❌ Ошибка верификации ($errorCode)"
//        }
//
//        onError(errorMessage)
//    }
//
//    private fun handleNetworkError(e: Exception, onError: (String) -> Unit) {
//        Log.e("NETWORK", "Сетевая ошибка: ${e.message}")
//
//        val errorMessage = when {
//            e.message?.contains("Unable to resolve host") == true ->
//                "❌ Нет подключения к интернету"
//            e.message?.contains("timeout") == true ->
//                "❌ Превышено время ожидания"
//            e.message?.contains("End of input") == true ->
//                "❌ Сервер вернул пустой ответ"
//            else -> "❌ Ошибка сети: ${e.message}"
//        }
//
//        onError(errorMessage)
//    }
//
//    // SharedPreferences методы
//    private fun saveRegistrationData(
//        email: String,
//        password: String,
//        firstName: String,
//        context: Context
//    ) {
//        val prefs = context.getSharedPreferences("reg_data", Context.MODE_PRIVATE)
//        prefs.edit()
//            .putString("email", email)
//            .putString("password", password)
//            .putString("firstname", firstName)
//            .apply()
//        Log.d("PREF", "Данные сохранены: $email")
//    }
//
//    private fun getRegistrationData(context: Context): Triple<String, String, String> {
//        val prefs = context.getSharedPreferences("reg_data", Context.MODE_PRIVATE)
//        return Triple(
//            prefs.getString("email", "") ?: "",
//            prefs.getString("password", "") ?: "",
//            prefs.getString("firstname", "") ?: ""
//        )
//    }
//
//    private fun saveAuthData(context: Context, userId: String, email: String) {
//        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        prefs.edit()
//            .putString("user_id", userId)
//            .putString("user_email", email)
//            .putBoolean("is_logged_in", true)
//            .apply()
//        Log.d("AUTH", "Auth данные сохранены: $userId")
//    }
//
//    private fun clearRegistrationData(context: Context) {
//        val prefs = context.getSharedPreferences("reg_data", Context.MODE_PRIVATE)
//        prefs.edit().clear().apply()
//        Log.d("PREF", "Временные данные очищены")
//    }
//
//    fun getUserId(context: Context): String {
//        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        return prefs.getString("user_id", "") ?: ""
//    }
//
//    fun isLoggedIn(context: Context): Boolean {
//        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        return prefs.getBoolean("is_logged_in", false)
//    }
//
//    fun signIn(
//        email: String,
//        password: String,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        isLoading = true
//
//        viewModelScope.launch {
//            try {
//                Log.d("SIGNIN", "Попытка входа: email=$email")
//
//                val response = RetrofitInstance.userManagementService.signIn(
//                    SignInRequest(email, password)
//                )
//
//                if (response.isSuccessful) {
//                    isLoading = false
//                    Log.d("SIGNIN", "Вход успешен")
//
//                    // Сохраняем токен если нужно
//                    val token = response.body()?.access_token
//                    if (token != null) {
//                        // saveTokenToPrefs(token, context) // если нужно сохранить токен
//                    }
//
//                    onSuccess()
//
//                } else {
//                    isLoading = false
//                    val errorBody = response.errorBody()?.string()
//                    Log.e("SIGNIN", "Ошибка входа: ${response.code()}, $errorBody")
//
//                    val error = when {
//                        errorBody?.contains("email_not_confirmed") == true ||
//                                errorBody?.contains("Email not confirmed") == true -> {
//                            // Если email не подтвержден, предлагаем перейти на верификацию
//                            "Подтвердите email перед входом"
//                        }
//                        errorBody?.contains("Invalid login") == true ||
//                                errorBody?.contains("invalid_credentials") == true ->
//                            "Неверный email или пароль"
//
//                        response.code() == 400 -> "Некорректные данные"
//                        response.code() == 429 -> "Слишком много попыток. Попробуйте позже"
//                        else -> "Ошибка авторизации"
//                    }
//                    onError(error)
//                }
//            } catch (e: Exception) {
//                isLoading = false
//                Log.e("SIGNIN", "Сетевая ошибка: ${e.message}")
//
//                val errorMsg = when {
//                    e.message?.contains("Unable to resolve host") == true ->
//                        "Отсутствует соединение с интернетом"
//                    e.message?.contains("timeout") == true ->
//                        "Превышено время ожидания"
//                    else -> "Ошибка сети"
//                }
//                onError(errorMsg)
//            }
//        }
//    }
//
//    // В SignUpViewModel добавьте:
//    private fun saveTokenToPrefs(token: String, context: Context) {
//        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        prefs.edit().putString("auth_token", token).apply()
//    }
//
//    fun getTokenFromPrefs(context: Context): String {
//        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        return prefs.getString("auth_token", "") ?: ""
//    }
//
//    // Вспомогательные методы
//    private fun saveEmailToPrefs(email: String, context: Context) {
//        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        prefs.edit().putString("pending_email", email).apply()
//        Log.d("PREF", "Email сохранен: $email")
//    }
//
//    fun getEmailFromPrefs(context: Context): String {
//        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        return prefs.getString("pending_email", "") ?: ""
//    }
//
//    fun getEmail(): String {
//        return savedEmail
//    }
//
//    private fun clearSavedData(context: Context) {
//        savedEmail = ""
//        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
//        prefs.edit().remove("pending_email").apply()
//        Log.d("PREF", "Данные очищены")
//    }
//}

//

package com.example.educationalpractice.ui.theme.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Data.Model.*
import com.example.educationalpractice.Data.Service.RetrofitInstance
import kotlinx.coroutines.launch
import java.util.*

class SignUpViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var savedEmail by mutableStateOf("")

    // === 1. РЕГИСТРАЦИЯ ===
    fun signUp(
        email: String,
        password: String,
        firstName: String,
        context: Context,
        onSuccess: (email: String) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("SIGNUP", "📧 Регистрация: $email")

                val response = RetrofitInstance.userManagementService.signUp(
                    SignUpRequest(email, password)
                )

                if (response.isSuccessful) {
                    savedEmail = email
                    saveUserData(email, password, firstName, context)

                    Log.d("SIGNUP", "✅ Регистрация успешна! OTP отправлен")
                    isLoading = false
                    onSuccess(email)

                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    val errorCode = response.code()
                    Log.e("SIGNUP", "❌ Ошибка: $errorCode, $errorBody")

                    val errorMsg = when {
                        errorCode == 400 && errorBody.contains("already registered", true) ->
                            "❌ Пользователь с таким email уже зарегистрирован"
                        errorCode == 400 -> "❌ Некорректные данные"
                        else -> "❌ Ошибка регистрации ($errorCode)"
                    }
                    isLoading = false
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                isLoading = false
                Log.e("SIGNUP", "❌ Exception: ${e.message}", e)
                onError("❌ Ошибка: ${e.message}")
            }
        }
    }

    // === 2. ВЕРИФИКАЦИЯ OTP ===
    fun verifyCode(
        code: String,
        context: Context,
        onSuccess: (userId: String) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                // 1. Получаем email
                val email = getEmailForVerification(context)
                if (email.isEmpty()) {
                    isLoading = false
                    onError("❌ Email не найден")
                    return@launch
                }

                Log.d("VERIFY", "🔐 Верификация: email=$email, code=$code")

                // 2. Проверяем OTP
                val response = RetrofitInstance.userManagementService.verify(
                    VerifyRequest(token = code, email = email)
                )

                if (response.isSuccessful) {
                    val verifyBody = response.body()
                    Log.d("VERIFY", "✅ OTP подтвержден! Ответ: $verifyBody")

                    // 3. Получаем userId из ответа
                    val userId = verifyBody?.user?.id

                    if (userId != null) {
                        Log.d("VERIFY", "✅ Получен userId из verify: $userId")
                        // Сразу создаем профиль
                        createProfileAfterVerification(userId, context, onSuccess, onError)
                    } else {
                        // Если userId нет, пробуем получить через signIn
                        Log.d("VERIFY", "⚠️ userId не найден, пробуем signIn")
                        tryGetUserIdAfterVerification(email, context, onSuccess, onError)
                    }

                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string() ?: ""
                    Log.e("VERIFY", "❌ Ошибка verify: $errorCode, $errorBody")

                    val errorMsg = when (errorCode) {
                        400 -> "❌ Неверный или устаревший код"
                        404 -> "❌ Пользователь не найден"
                        else -> "❌ Ошибка верификации ($errorCode)"
                    }

                    isLoading = false
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                isLoading = false
                Log.e("VERIFY", "❌ Exception: ${e.message}", e)
                onError("❌ Ошибка: ${e.message}")
            }
        }
    }

    // === 3. ПОЛУЧЕНИЕ userId ЧЕРЕЗ SIGNIN ПОСЛЕ ВЕРИФИКАЦИИ ===
    private suspend fun tryGetUserIdAfterVerification(
        email: String,
        context: Context,
        onSuccess: (userId: String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val (savedEmail, savedPassword, savedFirstName) = getUserData(context)

            if (savedPassword.isEmpty()) {
                isLoading = false
                onError("❌ Данные для входа не найдены")
                return
            }

            Log.d("USER_ID", "🔑 Пытаемся войти для получения userId")

            val signInResponse = RetrofitInstance.userManagementService.signIn(
                SignInRequest(savedEmail, savedPassword)
            )

            if (signInResponse.isSuccessful) {
                val userId = signInResponse.body()?.user?.id

                if (!userId.isNullOrEmpty()) {
                    Log.d("USER_ID", "✅ Получен userId через signIn: $userId")
                    createProfileAfterVerification(userId, context, onSuccess, onError)
                } else {
                    isLoading = false
                    onError("❌ Не удалось получить ID пользователя")
                }
            } else {
                isLoading = false
                val errorCode = signInResponse.code()
                Log.e("USER_ID", "❌ Ошибка signIn: $errorCode")
                onError("❌ Ошибка входа после верификации")
            }
        } catch (e: Exception) {
            isLoading = false
            Log.e("USER_ID", "❌ Exception: ${e.message}", e)
            onError("❌ Ошибка: ${e.message}")
        }
    }

    // === 4. СОЗДАНИЕ ПРОФИЛЯ ПОСЛЕ ВЕРИФИКАЦИИ ===
    private suspend fun createProfileAfterVerification(
        userId: String,
        context: Context,
        onSuccess: (userId: String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            Log.d("PROFILE", "=== НАЧАЛО СОЗДАНИЯ ПРОФИЛЯ ===")
            Log.d("PROFILE", "userId: $userId")

            // Получаем сохраненные данные
            val (savedEmail, savedPassword, savedFirstName) = getUserData(context)

            if (savedFirstName.isEmpty()) {
                Log.e("PROFILE", "❌ Имя не найдено в сохраненных данных")
                isLoading = false
                onError("❌ Данные профиля не найдены")
                return
            }

            val firstName = savedFirstName
            val email = savedEmail

            Log.d("PROFILE", "👤 Создаем профиль: userId=$userId, name=$firstName, email=$email")

            // 1. Подготавливаем данные профиля
            val profileData = mapOf<String, Any>(
                "id" to UUID.randomUUID().toString(),
                "user_id" to userId,
                "firstname" to firstName,
                "email" to email,
                "created_at" to System.currentTimeMillis().toString()
            )

            Log.d("PROFILE", "📤 Отправляем данные: $profileData")

            // 2. Создаем профиль в Supabase
            val response = RetrofitInstance.profileService.createProfile(profileData)

            Log.d("PROFILE", "📥 Получен ответ: код=${response.code()}, успешно=${response.isSuccessful()}")

            if (response.isSuccessful) {
                Log.d("PROFILE", "🎉 Профиль создан успешно!")

                // 3. Сохраняем данные авторизации
                saveAuthData(context, userId, email, firstName)

                // 4. Очищаем временные данные
                clearUserData(context)

                // 5. Обновляем поле ViewModel
                this.savedEmail = email

                isLoading = false
                onSuccess(userId)

            } else {
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string() ?: ""
                Log.e("PROFILE", "❌ Ошибка создания профиля: $errorCode, $errorBody")

                // Если профиль уже существует (409) - все равно успех
                if (errorCode == 409) {
                    Log.d("PROFILE", "⚠️ Профиль уже существует, продолжаем")
                    saveAuthData(context, userId, email, firstName)
                    clearUserData(context)

                    isLoading = false
                    onSuccess(userId)
                } else {
                    isLoading = false
                    onError("❌ Ошибка создания профиля ($errorCode)")
                }
            }
        } catch (e: Exception) {
            isLoading = false
            Log.e("PROFILE", "❌ Exception при создании профиля: ${e.message}", e)
            Log.e("PROFILE", "Stack trace:", e)
            onError("❌ Ошибка создания профиля: ${e.message}")
        }
    }

    // === 5. ВХОД ===
    fun signIn(
        email: String,
        password: String,
        context: Context,
        onSuccess: (userId: String) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        viewModelScope.launch {
            try {
                Log.d("SIGNIN", "🔑 Вход: $email")

                val response = RetrofitInstance.userManagementService.signIn(
                    SignInRequest(email, password)
                )

                if (response.isSuccessful) {
                    val userData = response.body()?.user
                    val userId = userData?.id ?: ""

                    if (userId.isNotEmpty()) {
                        // Сохраняем данные авторизации
                        saveAuthData(context, userId, email, "")
                        savedEmail = email

                        Log.d("SIGNIN", "✅ Вход успешен, userId: $userId")
                        isLoading = false
                        onSuccess(userId)
                    } else {
                        isLoading = false
                        onError("❌ Не удалось получить ID пользователя")
                    }

                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string() ?: ""
                    Log.e("SIGNIN", "❌ Ошибка: $errorCode, $errorBody")

                    val errorMsg = when {
                        errorCode == 400 -> "❌ Неверный email или пароль"
                        else -> "❌ Ошибка входа ($errorCode)"
                    }

                    isLoading = false
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                isLoading = false
                Log.e("SIGNIN", "❌ Exception: ${e.message}", e)
                onError("❌ Ошибка: ${e.message}")
            }
        }
    }

    // === ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===
    private fun saveUserData(email: String, password: String, firstName: String, context: Context) {
        val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("email", email)
            .putString("password", password)
            .putString("firstname", firstName)
            .apply()
        Log.d("SAVE", "📝 Сохранены данные: email=$email, name=$firstName")
    }

    private fun getUserData(context: Context): Triple<String, String, String> {
        val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        return Triple(
            prefs.getString("email", "") ?: "",
            prefs.getString("password", "") ?: "",
            prefs.getString("firstname", "") ?: ""
        )
    }

    private fun saveAuthData(context: Context, userId: String, email: String, firstName: String) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("user_id", userId)
            .putString("user_email", email)
            .putString("user_name", firstName)
            .putBoolean("is_logged_in", true)
            .putLong("login_time", System.currentTimeMillis())
            .apply()
        Log.d("AUTH", "🔐 Сохранены auth данные: userId=$userId, email=$email")
    }

    private fun clearUserData(context: Context) {
        val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        Log.d("CLEAN", "🧹 Временные данные очищены")
    }

    // === ПУБЛИЧНЫЕ МЕТОДЫ ===
    fun getEmailForVerification(context: Context): String {
        return if (savedEmail.isNotEmpty()) {
            savedEmail
        } else {
            val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            prefs.getString("email", "") ?: ""
        }
    }

    fun getUserId(context: Context): String {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("user_id", "") ?: ""
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getBoolean("is_logged_in", false)
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        savedEmail = ""
        Log.d("LOGOUT", "👋 Выход из системы")
    }
}
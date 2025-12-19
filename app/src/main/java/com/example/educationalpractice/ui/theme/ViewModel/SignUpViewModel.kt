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
import com.example.shoestore.data.RetrofitInstance
import com.example.shoestore.data.TokenStorage
import com.example.shoestore.data.model.*
import com.example.shoestore.data.repository.ProfileRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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
        errorMessage = ""

        viewModelScope.launch {
            try {
                Log.d("SIGNUP", "📧 Регистрация: $email")

                val response = RetrofitInstance.userManagementService.signUp(
                    SignUpRequest(email = email, password = password)
                )

                if (response.isSuccessful) {
                    savedEmail = email
                    saveUserData(email, password, firstName, context)

                    Log.d("SIGNUP", "✅ Регистрация успешна! OTP отправлен")
                    isLoading = false
                    onSuccess(email)

                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string() ?: ""
                    Log.e("SIGNUP", "❌ Ошибка: $errorCode, $errorBody")

                    val errorMsg = when {
                        errorCode == 400 && errorBody.contains("already registered", true) ->
                            "❌ Пользователь с таким email уже зарегистрирован"
                        errorCode == 400 -> "❌ Некорректные данные"
                        else -> "❌ Ошибка регистрации ($errorCode)"
                    }
                    isLoading = false
                    errorMessage = errorMsg
                    onError(errorMsg)
                }
            } catch (e: IOException) {
                isLoading = false
                errorMessage = "❌ Ошибка сети. Проверьте подключение"
                Log.e("SIGNUP", "❌ Сетевая ошибка: ${e.message}", e)
                onError("❌ Ошибка сети. Проверьте подключение")
            } catch (e: Exception) {
                isLoading = false
                errorMessage = "❌ Ошибка: ${e.message ?: "Неизвестная ошибка"}"
                Log.e("SIGNUP", "❌ Exception: ${e.message}", e)
                onError("❌ Ошибка: ${e.message ?: "Неизвестная ошибка"}")
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
        errorMessage = ""

        viewModelScope.launch {
            try {
                // 1. Получаем email
                val email = getEmailForVerification(context)
                if (email.isEmpty()) {
                    isLoading = false
                    errorMessage = "❌ Email не найден"
                    onError("❌ Email не найден")
                    return@launch
                }

                Log.d("VERIFY", "🔐 Верификация: email=$email, code=$code")

                // 2. Проверяем OTP используя правильный метод и модель
                val response = RetrofitInstance.userManagementService.verifyOtp(
                    VerifyOtpRequest(
                        email = email,
                        token = code,
                        type = "email"
                    )
                )

                if (response.isSuccessful) {
                    val verifyResponse = response.body()
                    val userId = verifyResponse?.user?.id ?: ""

                    Log.d("VERIFY", "✅ OTP подтвержден! Ответ: $verifyResponse")

                    if (userId.isNotEmpty()) {
                        // Сохраняем токен авторизации
                        verifyResponse?.access_token?.let { token ->
                            TokenStorage.saveToken(token)
                        }

                        // Создаем профиль после верификации
                        createProfileAfterVerification(userId, context, onSuccess, onError)
                    } else {
                        isLoading = false
                        errorMessage = "❌ Не удалось получить ID пользователя"
                        onError("❌ Не удалось получить ID пользователя")
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
                    errorMessage = errorMsg
                    onError(errorMsg)
                }
            } catch (e: IOException) {
                isLoading = false
                errorMessage = "❌ Ошибка сети. Проверьте подключение"
                Log.e("VERIFY", "❌ Сетевая ошибка: ${e.message}", e)
                onError("❌ Ошибка сети. Проверьте подключение")
            } catch (e: Exception) {
                isLoading = false
                errorMessage = "❌ Ошибка: ${e.message ?: "Неизвестная ошибка"}"
                Log.e("VERIFY", "❌ Exception: ${e.message}", e)
                onError("❌ Ошибка: ${e.message ?: "Неизвестная ошибка"}")
            }
        }
    }

    // === 3. СОЗДАНИЕ ПРОФИЛЯ ПОСЛЕ ВЕРИФИКАЦИИ ===
    private suspend fun createProfileAfterVerification(
        userId: String,
        context: Context,
        onSuccess: (userId: String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            Log.d("PROFILE", "Создаем профиль через Repository...")

            val (savedEmail, savedPassword, savedFirstName) = getUserData(context)

            if (savedFirstName.isEmpty()) {
                isLoading = false
                errorMessage = "❌ Имя не найдено"
                onError("❌ Имя не найдено")
                return
            }

            // Используем ProfileRepository (исправленный вариант из предыдущих сообщений)
            val repository = ProfileRepository()
            val success = repository.createProfile(
                userId = userId,
                firstname = savedFirstName,
                email = savedEmail
            )

            if (success) {
                Log.d("PROFILE", "🎉 Профиль создан успешно!")

                // Сохраняем данные авторизации
                saveAuthData(context, userId, savedEmail, savedFirstName)

                // Очищаем временные данные
                clearUserData(context)

                // Обновляем поле ViewModel
                this.savedEmail = savedEmail

                isLoading = false
                onSuccess(userId)
            } else {
                Log.e("PROFILE", "❌ Ошибка создания профиля")

                // Пробуем создать профиль с минимальными данными
                val minimalSuccess = repository.createProfile(
                    userId = userId,
                    firstname = savedFirstName
                )

                if (minimalSuccess) {
                    Log.d("PROFILE", "🎉 Профиль создан успешно (минимальные данные)!")
                    saveAuthData(context, userId, savedEmail, savedFirstName)
                    clearUserData(context)
                    isLoading = false
                    onSuccess(userId)
                } else {
                    isLoading = false
                    errorMessage = "❌ Ошибка создания профиля"
                    onError("❌ Ошибка создания профиля")
                }
            }
        } catch (e: Exception) {
            isLoading = false
            errorMessage = "❌ Ошибка создания профиля: ${e.message ?: "Неизвестная ошибка"}"
            Log.e("PROFILE", "❌ Exception: ${e.message}", e)
            onError("❌ Ошибка создания профиля: ${e.message ?: "Неизвестная ошибка"}")
        }
    }

    // === 4. ВХОД ===
    fun signIn(
        email: String,
        password: String,
        context: Context,
        onSuccess: (userId: String) -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                Log.d("SIGNIN", "🔑 Вход: $email")

                val response = RetrofitInstance.userManagementService.signIn(
                    SignInRequest(email = email, password = password)
                )

                if (response.isSuccessful) {
                    val signInResponse = response.body()
                    val userId = signInResponse?.user?.id ?: ""
                    val accessToken = signInResponse?.access_token ?: ""

                    if (userId.isNotEmpty() && accessToken.isNotEmpty()) {
                        // Сохраняем токен в хранилище
                        TokenStorage.saveToken(accessToken)

                        // Сохраняем данные авторизации
                        saveAuthData(context, userId, email, "")
                        savedEmail = email

                        Log.d("SIGNIN", "✅ Вход успешен, userId: $userId, token сохранен")
                        isLoading = false
                        onSuccess(userId)
                    } else {
                        isLoading = false
                        errorMessage = "❌ Не удалось получить данные пользователя"
                        onError("❌ Не удалось получить данные пользователя")
                    }

                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string() ?: ""
                    Log.e("SIGNIN", "❌ Ошибка: $errorCode, $errorBody")

                    val errorMsg = when {
                        errorCode == 400 -> "❌ Неверный email или пароль"
                        errorCode == 422 -> "❌ Email не подтвержден"
                        else -> "❌ Ошибка входа ($errorCode)"
                    }

                    isLoading = false
                    errorMessage = errorMsg
                    onError(errorMsg)
                }
            } catch (e: IOException) {
                isLoading = false
                errorMessage = "❌ Ошибка сети. Проверьте подключение"
                Log.e("SIGNIN", "❌ Сетевая ошибка: ${e.message}", e)
                onError("❌ Ошибка сети. Проверьте подключение")
            } catch (e: Exception) {
                isLoading = false
                errorMessage = "❌ Ошибка: ${e.message ?: "Неизвестная ошибка"}"
                Log.e("SIGNIN", "❌ Exception: ${e.message}", e)
                onError("❌ Ошибка: ${e.message ?: "Неизвестная ошибка"}")
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
        TokenStorage.clearToken() // Очищаем токен из хранилища
        Log.d("LOGOUT", "👋 Выход из системы, токен очищен")
    }
}
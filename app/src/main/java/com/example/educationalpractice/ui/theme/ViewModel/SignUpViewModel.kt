package com.example.educationalpractice.ui.theme.ViewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.size
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import androidx.core.content.edit
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.educationalpractice.Data.Model.SignInRequest
import com.example.educationalpractice.Data.Model.SignUpRequest
import com.example.educationalpractice.Data.Model.VerifyRequest
import com.example.educationalpractice.Data.RetrofitInstance
import com.example.myfirstproject.data.service.UserManagementService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    var isLoading = false
    var errorMessage = ""
    var pendingEmail = ""
    var pendingToken: String? = null
    var pendingPassword = ""
    private var savedEmail = ""

    // Регистрация
    fun signUp(
        email: String,
        password: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.signUp(
                    SignUpRequest(email, password)
                )

                if (response.isSuccessful) {
                    signInAfterRegistration(email, password, onSuccess, onError)
                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    when {
                        errorBody?.contains("already registered") == true ->
                            onError("Пользователь уже зарегистрирован")
                        errorBody?.contains("weak_password") == true ->
                            onError("Пароль слишком слабый")
                        else -> onError("Ошибка регистрации: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                isLoading = false
                if (e.message?.contains("Unable to resolve host") == true) {
                    onError("Отсутствует соединение с интернетом")
                } else {
                    onError("Ошибка сети: ${e.message}")
                }
            }
        }
    }

    private fun signInAfterRegistration(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.signIn(
                    SignInRequest(email, password)
                )

                if (response.isSuccessful) {
                    isLoading = false
                    onSuccess()
                } else {
                    isLoading = false
                    onSuccess()
                }
            } catch (e: Exception) {
                isLoading = false
                onSuccess()
            }
        }
    }

    fun verifyCode(code: String, context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            try {
                val email = savedEmail.ifEmpty { getEmailFromPrefs(context) }

                if (email.isEmpty()) {
                    onError("Email не найден")
                    return@launch
                }

                val response = RetrofitInstance.userManagementService.verify(
                    VerifyRequest(token = code, email = email)
                )

                if (response.isSuccessful) {
                    isLoading = false
                    onSuccess()
                } else {
                    isLoading = false
                    onError("Неверный код")
                }
            } catch (e: Exception) {
                isLoading = false
                onError("Ошибка сети: ${e.message}")
            }
        }
    }

    private fun saveEmailToPrefs(email: String, context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().putString("pending_email", email).apply()
    }

    fun getEmailFromPrefs(context: Context): String {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("pending_email", "") ?: ""
    }

    // Метод для получения email
    fun getEmail(): String {
        return savedEmail
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.signIn(
                    SignInRequest(
                        email,
                        password
                    )
                )

                if (response.isSuccessful) {
                    isLoading = false
                    onSuccess()
                } else {
                    isLoading = false
                    val errorBody = response.errorBody()?.string()
                    if (errorBody?.contains("email_not_confirmed") == true ||
                        errorBody?.contains("Email not confirmed") == true) {
                        onError("Подтвердите email перед входом")
                    } else {
                        onError("Неверный email или пароль")
                    }
                }
            } catch (e: Exception) {
                isLoading = false
                onError("Ошибка сети")
            }
        }
    }
}
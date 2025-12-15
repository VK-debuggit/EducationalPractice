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

//class SignUpViewModel(private val database: AppDatabase) : ViewModel() {
class SignUpViewModel : ViewModel() {
    var isLoading = false
    var errorMessage = ""
    var pendingEmail = ""
    var pendingToken: String? = null
    var pendingPassword = ""
    private var savedEmail = ""

    fun signUp(email: String, password: String, context: Context, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.signUp(
                    SignUpRequest(
                        email,
                        password
                    )
                )

                if (response.isSuccessful) {
                    // Сохраняем email в SharedPreferences и в переменную
                    saveEmailToPrefs(email, context)
                    savedEmail = email
                    isLoading = false
                    onSuccess(email)
                } else {
                    isLoading = false
                    onError("Ошибка регистрации")
                }
            } catch (e: Exception) {
                isLoading = false
                onError("Ошибка сети: ${e.message}")
            }
        }
    }

    fun verifyCode(code: String, context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        isLoading = true
        viewModelScope.launch {
            try {
                // Используем сохраненный email
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

    // Метод для получения email (для отображения)
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
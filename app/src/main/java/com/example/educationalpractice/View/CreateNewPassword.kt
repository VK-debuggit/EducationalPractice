package com.example.educationalpractice.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educationalpractice.Data.CustomAlertDialog
import com.example.educationalpractice.Data.CustomButton
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.*
import com.example.educationalpractice.ui.theme.ViewModel.ResetPasswordViewModel

@Composable
fun CreateNewPassword() {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: ResetPasswordViewModel = viewModel()

    // Функция для проверки пароля
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    // Функция для сброса пароля
    fun resetPassword() {
        if (newPassword.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Заполните все поля"
            showErrorDialog = true
            return
        }

        if (!isValidPassword(newPassword)) {
            errorMessage = "Пароль должен содержать минимум 6 символов"
            showErrorDialog = true
            return
        }

        if (newPassword != confirmPassword) {
            errorMessage = "Пароли не совпадают"
            showErrorDialog = true
            return
        }

        // Обновляем пароль через ViewModel
        viewModel.resetPassword(
            newPassword = newPassword,
            onSuccess = {
                showSuccessDialog = true
            },
            onError = { error ->
                errorMessage = error
                showErrorDialog = true
            }
        )
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Spacer(Modifier.weight(0.1f))

        Image(
            painter = painterResource(id = R.drawable.iconback),
            contentDescription = "Назад",
            modifier = Modifier.clickable {
                NavigationManager.navigateTo(Views.Verification.route)
            }
        )

        Spacer(Modifier.weight(0.1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Новый пароль",
                color = Text,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Придумайте новый пароль",
                color = SubTextDark,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(0.1f))

        // Поле для нового пароля
        Text(
            text = "Новый пароль",
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Background)
                .fillMaxWidth(),
            value = newPassword,
            onValueChange = { newPassword = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text("Введите новый пароль") },
            singleLine = true,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.eyeopen else R.drawable.eyeclose
                        ),
                        contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.weight(0.05f))

        Text(
            text = "Подтвердите пароль",
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Background)
                .fillMaxWidth(),
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text("Повторите новый пароль") },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(
                            id = if (confirmPasswordVisible) R.drawable.eyeopen else R.drawable.eyeclose
                        ),
                        contentDescription = if (confirmPasswordVisible) "Скрыть пароль" else "Показать пароль"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(Modifier.weight(0.1f))

        // Кнопка сброса пароля
        CustomButton(
            onClick = { resetPassword() },
            text = if (viewModel.isLoading) "Обновление..." else "Обновить пароль",
            enabled = !viewModel.isLoading
        )

        Spacer(Modifier.weight(0.8f))
    }

    // Диалог успеха
    if (showSuccessDialog) {
        CustomAlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                NavigationManager.navigateTo(Views.SignIn.route)
            },
            dialogTitle = "Успешно",
            dialogText = "Пароль успешно обновлен",
            iconResId = R.drawable.emailotp,
            confirmButtonText = "OK",
            onConfirmButtonClick = {
                showSuccessDialog = false
                NavigationManager.navigateTo(Views.SignIn.route)
            }
        )
    }

    // Диалог ошибки
    if (showErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = { showErrorDialog = false },
            dialogTitle = "Ошибка",
            dialogText = errorMessage,
            iconResId = null,
            confirmButtonText = "OK",
            onConfirmButtonClick = { showErrorDialog = false }
        )
    }
}
package com.example.educationalpractice.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educationalpractice.Data.CustomAlertDialog
import com.example.educationalpractice.Data.CustomButton
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.*
import com.example.educationalpractice.ui.theme.ViewModel.SignUpViewModel

@Composable
fun RegisterAccount() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showEmailErrorDialog by remember { mutableStateOf(false) }

    // Единое состояние для отображения ошибок
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorTitle by remember { mutableStateOf("Ошибка") }
    var errorMessages by remember { mutableStateOf(emptyList<String>()) }

    // Функция для показа ошибок
    fun showError(title: String, messages: List<String>) {
        errorTitle = title
        errorMessages = messages
        showErrorDialog = true
    }

    // Функция для показа ошибок сети/сервера
    fun showNetworkError(error: String) {
        showError("Ошибка соединения", listOf(error))
    }

    // Функция для показа ошибок валидации
    fun showValidationError(message: String) {
        showError("Ошибка заполнения", listOf(message))
    }

    // Функция валидации email по паттерну "name@domenname.ru"
    fun isValidEmail(email: String): Boolean {
        val pattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}\$".toRegex()
        return pattern.matches(email)
    }

    if (showEmailErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = { showEmailErrorDialog = false },
            dialogTitle = "Некорректный email",
            dialogText = "Email должен быть в формате: name@domenname.ru",
            iconResId = null
        )
    }

    val context = LocalContext.current
    val viewModel: SignUpViewModel = viewModel()

    // Используем поле isLoading из ViewModel
    var isLoading by remember { mutableStateOf(false) }

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
            modifier = Modifier
                .clickable(
                    enabled = !isLoading,
                    onClick = {
                        NavigationManager.navigateTo(Views.SignIn.route)
                    }
                )
        )

        Spacer(Modifier.weight(0.1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome_mes),
                color = Text,
                fontSize = 32.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.data),
                color = SubTextDark,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(0.1f))

        // Поле для имени
        Text(
            text = stringResource(R.string.Your),
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Background)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(14.dp),
            value = name,
            placeholder = { Text("xxxxxxxx") },
            onValueChange = { name = it },
            enabled = !isLoading
        )

        Spacer(Modifier.weight(0.1f))

        Text(
            text = stringResource(R.string.email),
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Background)
                .fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(14.dp),
            placeholder = { Text("xyz@gmail.com") },
            enabled = !isLoading,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.weight(0.1f))

        Text(
            text = stringResource(R.string.password),
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Background)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Background,
                unfocusedContainerColor = Background,
                disabledContainerColor = Background,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    enabled = !isLoading
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.eyeopen else R.drawable.eyeclose
                        ),
                        contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль"
                    )
                }
            },
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(14.dp),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            enabled = !isLoading,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(Modifier.weight(0.05f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (!isLoading) isAgreed = !isAgreed
                },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (isAgreed) Accent else Color.Transparent,
                        RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isAgreed) Accent else Color.Gray,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.agree),
                    contentDescription = "Согласен",
                    modifier = Modifier.size(10.dp, 10.dp),
                    colorFilter = ColorFilter.tint(
                        if (isAgreed) Color.White else Color.Gray
                    )
                )
            }

            Spacer(Modifier.width(15.dp))

            Text(
                text = stringResource(R.string.Agree),
                color = SubTextDark,
                style = TextStyle(
                    textDecoration = TextDecoration.Underline
                ),
                fontSize = 16.sp,
                modifier = Modifier.clickable {
                    if (!isLoading) isAgreed = !isAgreed
                }
            )
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            CustomButton(
                onClick = {
                    if (email.isBlank() || password.isBlank() || name.isBlank()) {
                        showValidationError("Пожалуйста, заполните все поля")
                    } else if (!isAgreed) {
                        showValidationError("Необходимо согласиться с условиями")
                    } else if (password.length < 6) {
                        showValidationError("Пароль должен содержать не менее 6 символов")
                    } else if (!email.contains("@") || !email.contains(".")) {
                        showValidationError("Введите корректный email адрес")
                    } else {
                        isLoading = true

                        viewModel.signUp(
                            email = email,
                            password = password,
                            context = context,
                            onSuccess = {
                                isLoading = false
                                // Переходим на Verification после успешной регистрации
                                NavigationManager.navigateTo(Views.Verification.route)
                            },
                            onError = { error ->
                                isLoading = false
                                // Определяем тип ошибки для красивого отображения
                                val errorText = when {
                                    error.contains("отсутствует соединение", ignoreCase = true) ||
                                            error.contains("unable to resolve host", ignoreCase = true) ||
                                            error.contains("network", ignoreCase = true) ->
                                        "Отсутствует соединение с интернетом"

                                    error.contains("already registered", ignoreCase = true) ||
                                            error.contains("already exists", ignoreCase = true) ->
                                        "Пользователь с таким email уже зарегистрирован"

                                    error.contains("weak password", ignoreCase = true) ||
                                            error.contains("пароль слишком", ignoreCase = true) ->
                                        "Пароль слишком слабый. Используйте более сложный пароль"

                                    error.contains("timeout", ignoreCase = true) ||
                                            error.contains("timed out", ignoreCase = true) ->
                                        "Превышено время ожидания ответа от сервера"

                                    error.contains("server", ignoreCase = true) ||
                                            error.contains("сервер", ignoreCase = true) ->
                                        "Ошибка сервера. Попробуйте позже"

                                    else -> error
                                }

                                showNetworkError(errorText)
                            }
                        )
                    }
                },
                text = if (isLoading) "" else stringResource(R.string.Sign),
                enabled = !isLoading && isAgreed && email.isNotBlank() && password.isNotBlank() && name.isNotBlank(),
                cornerRadius = 14,
                modifier = Modifier.fillMaxWidth()
            )

            // Индикатор загрузки
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }
        }

        Spacer(Modifier.weight(0.5f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Already),
                color = Hint,
                fontSize = 16.sp
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.Invite),
                color = Text,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable(
                        enabled = !isLoading,
                        onClick = {
                            NavigationManager.navigateTo(Views.SignIn.route)
                        }
                    )
            )
        }

        Spacer(Modifier.weight(0.1f))
    }

    // Единый диалог для всех ошибок
    if (showErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = { showErrorDialog = false },
            dialogTitle = errorTitle,
            dialogText = errorMessages.joinToString("\n\n"),
            iconResId = null,
            confirmButtonText = "ОК"
        )
    }
}

@Preview
@Composable
private fun RegisterAccountPreview() {
    EducationalPracticeTheme() {
        RegisterAccount()
    }
}
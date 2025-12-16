package com.example.educationalpractice.View

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
fun SignIn() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorTitle by remember { mutableStateOf("Ошибка") }
    var errorMessage by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: SignUpViewModel = viewModel()

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
                    onClick = {
                        NavigationManager.navigateBack()
                    }
                )
        )

        Spacer(Modifier.weight(0.1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вход в аккаунт",
                style = MaterialTheme.typography.displayMedium, // Heading Regular 32
                color = Text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Введите ваши данные",
                style = MaterialTheme.typography.bodySmall, // Body Regular 16
                color = SubTextDark,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(0.1f))

        // Поле для email
        Text(
            text = "Email",
            style = MaterialTheme.typography.headlineSmall, // Heading SemiBold 16
            color = Text
        )

        Spacer(Modifier.height(8.dp))

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
            placeholder = {
                Text(
                    "xyz@gmail.com",
                    style = MaterialTheme.typography.bodySmall // Body Regular 16
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.weight(0.1f))

        // Поле для пароля
        Text(
            text = "Пароль",
            style = MaterialTheme.typography.headlineSmall, // Heading SemiBold 16
            color = Text
        )

        Spacer(Modifier.height(8.dp))

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
                    onClick = { passwordVisible = !passwordVisible }
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
            placeholder = {
                Text(
                    "Введите пароль",
                    style = MaterialTheme.typography.bodySmall // Body Regular 16
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(Modifier.weight(0.05f))

        // Забыли пароль?
        Text(
            text = "Забыли пароль?",
            style = MaterialTheme.typography.bodySmall.copy( // Body Regular 16
                color = Accent,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // TODO: Переход на экран восстановления пароля
                }
                .padding(vertical = 8.dp),
            textAlign = TextAlign.End
        )

        Spacer(Modifier.weight(0.1f))

        // Кнопка входа
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    color = Accent,
                    strokeWidth = 2.dp
                )
            } else {
                CustomButton(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            showErrorDialog = true
                            errorTitle = "Ошибка"
                            errorMessage = "Заполните все поля"
                        } else if (!email.contains("@") || !email.contains(".")) {
                            showErrorDialog = true
                            errorTitle = "Ошибка"
                            errorMessage = "Введите корректный email"
                        } else if (password.length < 6) {
                            showErrorDialog = true
                            errorTitle = "Ошибка"
                            errorMessage = "Пароль должен содержать не менее 6 символов"
                        } else {
                            // Вызов метода авторизации
                            viewModel.signIn(
                                email = email,
                                password = password,
                                onSuccess = {
                                    // Показываем успешный алерт
                                    showSuccessDialog = true
                                },
                                onError = { error ->
                                    showErrorDialog = true
                                    errorTitle = "Ошибка авторизации"
                                    errorMessage = error
                                }
                            )
                        }
                    },
                    text = "Войти",
                    enabled = !viewModel.isLoading && email.isNotBlank() && password.isNotBlank(),
                    cornerRadius = 14,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.weight(0.3f))

        // Ссылка на регистрацию
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ещё нет аккаунта? ",
                style = MaterialTheme.typography.bodySmall, // Body Regular 16
                color = Hint
            )
            Text(
                text = "Зарегистрироваться",
                style = MaterialTheme.typography.bodySmall, // Body Regular 16
                color = Text,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            NavigationManager.navigateTo(Views.RegisterAccount.route)
                        }
                    )
            )
        }

        Spacer(Modifier.weight(0.1f))
    }

    // Диалог ошибки
    if (showErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = { showErrorDialog = false },
            dialogTitle = errorTitle,
            dialogText = errorMessage,
            iconResId = null,
            confirmButtonText = "OK"
        )
    }

    // Диалог успешной авторизации
    if (showSuccessDialog) {
        CustomAlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            dialogTitle = "Успешно!",
            dialogText = "Авторизация прошла успешно!",
            iconResId = R.drawable.favorite_fill, // Добавьте иконку успеха если есть
            confirmButtonText = "OK",
            onConfirmButtonClick = {
                showSuccessDialog = false
                // Можно добавить переход на главный экран
                // NavigationManager.navigateTo(Views.Main.route)
            }
        )
    }
}

@Preview
@Composable
private fun SignInPreview() {
    EducationalPracticeTheme() {
        SignIn()
    }
}
package com.example.educationalpractice.View

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educationalpractice.Data.CustomAlertDialog
import com.example.educationalpractice.Data.CustomButton
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Background
import com.example.educationalpractice.ui.theme.Disable
import com.example.educationalpractice.ui.theme.Hint
import com.example.educationalpractice.ui.theme.Text as ThemeText
import com.example.educationalpractice.ui.theme.SubTextDark
import com.example.educationalpractice.ui.theme.ViewModel.SignUpViewModel

@Composable
fun SignIn() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Состояния для диалогов ошибок
    var showEmptyFieldsError by remember { mutableStateOf(false) }
    var showEmailErrorDialog by remember { mutableStateOf(false) }
    var showLoginErrorDialog by remember { mutableStateOf(false) }
    var loginErrorTitle by remember { mutableStateOf("") }
    var loginErrorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val viewModel: SignUpViewModel = viewModel()

    // Функция валидации email по паттерну "name@domenname.ru"
    fun isValidEmail(email: String): Boolean {
        val pattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}\$".toRegex()
        return pattern.matches(email)
    }

    // Диалог для ошибки пустых полей
    if (showEmptyFieldsError) {
        CustomAlertDialog(
            onDismissRequest = { showEmptyFieldsError = false },
            dialogTitle = "Пустые поля",
            dialogText = "Пожалуйста, заполните все поля",
        )
    }

    // Диалог для ошибки формата email
    if (showEmailErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = { showEmailErrorDialog = false },
            dialogTitle = "Некорректный email",
            dialogText = "Email должен быть в формате: name@domenname.ru\n\n" +
                    "• name - только маленькие буквы и цифры\n" +
                    "• domenname - только маленькие буквы и цифры\n" +
                    "• ru - только буквы (минимум 2 символа)\n\n",
        )
    }

    // Диалог для ошибки входа
    if (showLoginErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = { showLoginErrorDialog = false },
            dialogTitle = loginErrorTitle,
            dialogText = loginErrorMessage,
        )
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(20.dp)
            .fillMaxSize()
    ) {
        // Кнопка "Назад" вверху
        Spacer(Modifier.weight(0.05f))
        Image(
            painter = painterResource(id = R.drawable.iconback),
            contentDescription = "Назад",
            modifier = Modifier
                .clickable(
                    enabled = !isLoading,
                    onClick = {
                        NavigationManager.navigateBack()
                    }
                )
                .size(24.dp)
                .padding(start = 4.dp)
        )
        Spacer(Modifier.weight(0.05f))

        // Заголовок экрана
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.hello),
                color = ThemeText,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = stringResource(R.string.data),
                color = SubTextDark,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.weight(0.1f))

        // Поле ввода email
        Text(
            text = stringResource(R.string.email),
            color = ThemeText,
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
                focusedIndicatorColor = Accent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = ThemeText,
                unfocusedTextColor = ThemeText,
                disabledTextColor = Disable
            ),
            shape = RoundedCornerShape(14.dp),
            placeholder = {
                Text(
                    text = "xyz@gmail.com",
                    color = Hint
                )
            },
            enabled = !isLoading,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(Modifier.weight(0.1f))

        // Поле ввода пароля
        Text(
            text = stringResource(R.string.password),
            color = ThemeText,
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
                focusedIndicatorColor = Accent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = ThemeText,
                unfocusedTextColor = ThemeText,
                disabledTextColor = Disable
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
                        contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
                        tint = if (isLoading) Disable else Hint
                    )
                }
            },
            value = password,
            onValueChange = { password = it },
            shape = RoundedCornerShape(14.dp),
            placeholder = {
                Text(
                    text = "Введите пароль",
                    color = Hint
                )
            },
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

        Spacer(Modifier.weight(0.02f))

        // Ссылка "Забыли пароль?"
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Recovery),
                color = Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable(
                        enabled = !isLoading,
                        onClick = {
                            NavigationManager.navigateTo(Views.ForgotPassword.route)
                        }
                    )
            )
        }

        Spacer(Modifier.weight(0.03f))

        // Кнопка входа
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            CustomButton(
                onClick = {
                    // Проверка на пустые поля
                    if (email.isBlank() || password.isBlank()) {
                        showEmptyFieldsError = true
                        return@CustomButton
                    }

                    // Проверка формата email
                    if (!isValidEmail(email)) {
                        showEmailErrorDialog = true
                        return@CustomButton
                    }

                    // Начало процесса входа
                    isLoading = true

                    viewModel.signIn(
                        email = email,
                        password = password,
                        onSuccess = {
                            isLoading = false
                            Log.d("SignIn", "Успешный вход")

                            // Показываем Toast об успешном входе
                            Toast.makeText(
                                context,
                                "Добро пожаловать!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Переходим на главный экран
                            NavigationManager.navigateTo(Views.Verification.route)
                        },
                        onError = { error ->
                            isLoading = false
                            loginErrorTitle = "Ошибка входа"
                            loginErrorMessage = error
                            showLoginErrorDialog = true

                            // Логируем ошибку
                            Log.e("SignIn", "Ошибка входа: $error")
                        }
                    )
                },
                text = if (isLoading) "" else stringResource(R.string.SignIn),
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                cornerRadius = 14,
                modifier = Modifier.fillMaxWidth()
            )

            // Индикатор загрузки внутри кнопки
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    color = White,
                    strokeWidth = 2.dp
                )
            }
        }

        Spacer(Modifier.weight(0.5f))

        // Ссылка для перехода к регистрации
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.NewUser),
                color = Hint,
                fontSize = 16.sp
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.Create),
                color = Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable(
                        enabled = !isLoading,
                        onClick = {
                            NavigationManager.navigateTo(Views.RegisterAccount.route)
                        }
                    )
            )
        }
    }
}

@Preview
@Composable
private fun SignInPreview() {
    EducationalPracticeTheme {
        SignIn()
    }
}
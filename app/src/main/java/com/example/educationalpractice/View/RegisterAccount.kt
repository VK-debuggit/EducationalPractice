package com.example.educationalpractice.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.educationalpractice.Data.Components.CustomAlertDialog
import com.example.educationalpractice.Data.Components.CustomButton
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

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorTitle by remember { mutableStateOf("Ошибка") }
    var errorMessage by remember { mutableStateOf("") }

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
                style = MaterialTheme.typography.displayMedium,
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
            onValueChange = { name = it }
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
                    isAgreed = !isAgreed
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
                    isAgreed = !isAgreed
                }
            )
        }

        Spacer(Modifier.height(24.dp))

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
                        // Валидация полей
                        if (email.isBlank() || password.isBlank() || name.isBlank()) {
                            showErrorDialog = true
                            errorTitle = "Ошибка заполнения"
                            errorMessage = "Пожалуйста, заполните все поля"
                        } else if (!isAgreed) {
                            showErrorDialog = true
                            errorTitle = "Ошибка"
                            errorMessage = "Необходимо согласиться с условиями"
                        } else if (password.length < 6) {
                            showErrorDialog = true
                            errorTitle = "Ошибка"
                            errorMessage = "Пароль должен содержать не менее 6 символов"
                        } else if (!email.contains("@") || !email.contains(".")) {
                            showErrorDialog = true
                            errorTitle = "Ошибка"
                            errorMessage = "Введите корректный email адрес"
                        } else {
                            // Вызов метода регистрации
                            viewModel.signUp(
                                email = email,
                                password = password,
                                context = context,
                                onSuccess = { email ->
                                    // После успешной регистрации переходим на верификацию
                                    NavigationManager.navigateTo(Views.Verification.route)
                                },
                                onError = { error ->
                                    showErrorDialog = true
                                    errorTitle = "Ошибка регистрации"
                                    errorMessage = error
                                }
                            )
                        }
                    },
                    text = stringResource(R.string.Sign),
                    enabled = !viewModel.isLoading && isAgreed && email.isNotBlank() &&
                            password.isNotBlank() && name.isNotBlank(),
                    cornerRadius = 14,
                    modifier = Modifier.fillMaxWidth()
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
                        onClick = {
                            NavigationManager.navigateTo(Views.SignIn.route)
                        }
                    )
            )
        }

        Spacer(Modifier.weight(0.1f))
    }

    if (showErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = { showErrorDialog = false },
            dialogTitle = errorTitle,
            dialogText = errorMessage,
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
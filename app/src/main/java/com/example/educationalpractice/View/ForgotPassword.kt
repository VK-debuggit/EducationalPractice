package com.example.educationalpractice.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educationalpractice.Data.Components.CustomAlertDialog
import com.example.educationalpractice.Data.Components.CustomButton
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.*
import com.example.educationalpractice.ui.theme.ViewModel.ForgotPasswordViewModel

@Composable
fun ForgotPassword() {
    var email by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val viewModel: ForgotPasswordViewModel = viewModel()

    fun isValidEmail(email: String): Boolean {
        val pattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,}\$".toRegex()
        return pattern.matches(email)
    }

    fun showError(message: String) {
        errorMessage = message
        showErrorDialog = true
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
                NavigationManager.navigateTo(Views.SignIn.route)
            }
        )

        Spacer(Modifier.weight(0.1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.Forgot),
                color = Text,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.Enter),
                color = SubTextDark,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(0.1f))

        Text(
            text = "Email",
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
            isError = email.isNotEmpty() && !isValidEmail(email)
        )

        Spacer(Modifier.weight(0.1f))

        CustomButton(
            onClick = {
                if (email.isBlank()) {
                    showError("Пожалуйста, введите email")
                } else if (!isValidEmail(email)) {
                    showError("Введите корректный email в формате: name@domenname.ru")
                } else {
                    viewModel.sendPasswordResetEmail(
                        email = email,
                        onSuccess = {
                            showSuccessDialog = true
                        },
                        onError = { error ->
                            showError(error)
                        }
                    )
                }
            },
            text = if (viewModel.isLoading) "Отправка..." else stringResource(R.string.Send),
            enabled = !viewModel.isLoading
        )

        Spacer(Modifier.weight(0.8f))
    }

    if (showSuccessDialog) {
        Dialog(
            onDismissRequest = {
                showSuccessDialog = false
                NavigationManager.navigateTo(Views.Verification.route)
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        showSuccessDialog = false
                        NavigationManager.navigateTo(Views.Verification.route)
                    },
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.emailotp),
                        contentDescription = "Успех",
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.Check),
                        color = Text,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.WeHave),
                        color = SubTextDark,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }

    if (showErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = { showErrorDialog = false },
            dialogTitle = "Ошибка",
            dialogText = errorMessage,
            iconResId = null,
            onConfirmButtonClick = { showErrorDialog = false }
        )
    }
}
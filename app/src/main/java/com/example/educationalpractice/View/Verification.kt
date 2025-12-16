package com.example.educationalpractice.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.educationalpractice.ui.theme.ViewModel.VerificationViewModel
import kotlinx.coroutines.delay

@Composable
fun Verification() {
    val otpFields = remember { Array(6) { mutableStateOf("") } }
    val focusRequesters = remember { Array(6) { FocusRequester() } }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var timeRemaining by remember { mutableStateOf(60) }
    var isTimerActive by remember { mutableStateOf(true) }
    val canResend by remember { derivedStateOf { timeRemaining == 0 } }

    var showOtpError by remember { mutableStateOf(false) }

    val viewModel: VerificationViewModel = viewModel()

    LaunchedEffect(isTimerActive) {
        if (isTimerActive) {
            while (timeRemaining > 0) {
                delay(1000)
                timeRemaining--
            }
            isTimerActive = false
        }
    }

    fun getFullOtpCode(): String = otpFields.joinToString("") { it.value }
    fun isOtpComplete(): Boolean = otpFields.all { it.value.isNotBlank() }

    fun verifyOtpCode() {
        val otpCode = getFullOtpCode()

        if (otpCode.length != 6) {
            showOtpError = true
            errorMessage = "Введите все 6 цифр кода"
            showErrorDialog = true
            return
        }

        isTimerActive = false

        viewModel.verifyOtpCode(
            code = otpCode,
            onSuccess = {
                NavigationManager.navigateTo(Views.CreateNewPassword.route)
            },
            onError = { error ->
                showOtpError = true
                errorMessage = error
                showErrorDialog = true
            }
        )
    }

    fun resendOtpCode() {
        if (canResend) {
            isTimerActive = false
            viewModel.resendOtpCode(
                onSuccess = {
                    timeRemaining = 60
                    isTimerActive = true
                    otpFields.forEach { it.value = "" }
                    showOtpError = false
                    focusRequesters[0].requestFocus()
                },
                onError = { error ->
                    errorMessage = error
                    showErrorDialog = true
                    isTimerActive = true
                }
            )
        }
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
                NavigationManager.navigateTo(Views.ForgotPassword.route)
            }
        )

        Spacer(Modifier.weight(0.1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.OTP),
                color = Text,
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.Please),
                color = SubTextDark,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(0.1f))

        Text(
            text = stringResource(R.string.OTPCode),
            color = Text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0 until 6) {
                OtpDigitBox(
                    value = otpFields[i].value,
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                            otpFields[i].value = newValue
                            showOtpError = false

                            if (newValue.isNotEmpty() && i < 5) {
                                focusRequesters[i + 1].requestFocus()
                            }

                            if (i == 5 && isOtpComplete()) {
                                verifyOtpCode()
                            }
                        } else if (newValue.isEmpty() && i > 0) {
                            otpFields[i].value = ""
                            focusRequesters[i - 1].requestFocus()
                        }
                    },
                    focusRequester = focusRequesters[i],
                    isError = showOtpError
                )
            }
        }

        Spacer(Modifier.weight(0.1f))

        CustomButton(
            onClick = { verifyOtpCode() },
            text = if (viewModel.isLoading) "Проверка..." else "Подтвердить",
            enabled = isOtpComplete() && !viewModel.isLoading
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Не пришел код?",
            color = SubTextDark,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = if (canResend) {
                "Отправить код повторно"
            } else {
                val minutes = timeRemaining / 60
                val seconds = timeRemaining % 60
                "Отправить повторно через ${String.format("%02d:%02d", minutes, seconds)}"
            },
            color = if (canResend) Accent else SubTextDark,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = canResend && !viewModel.isLoading, onClick = { resendOtpCode() })
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(0.8f))
    }

    if (showErrorDialog) {
        CustomAlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                showOtpError = false
            },
            dialogTitle = "Ошибка",
            dialogText = errorMessage,
            iconResId = null,
            confirmButtonText = "OK",
            onConfirmButtonClick = {
                showErrorDialog = false
                showOtpError = false
            }
        )
    }
}

@Composable
fun OtpDigitBox(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    isError: Boolean
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .width(46.dp)
            .height(99.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isError) Color(0xFFFFEBEE) else Background,
                RoundedCornerShape(14.dp)
            )
            .focusRequester(focusRequester),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isError) Color.Red else Text
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = if (isError) 2.dp else 0.dp,
                        color = if (isError) Color.Red else Color.Transparent,
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = "•",
                        color = if (isError) Color.Red else SubTextDark,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                innerTextField()
            }
        }
    )
}
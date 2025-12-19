package com.example.educationalpractice.Data.Model

data class VerifyOtpResponse(
    val access_token: String,
    val user: UserData
)

data class UserData(
    val id: String,
    val email: String
)
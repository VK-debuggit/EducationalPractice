package com.example.educationalpractice.Data.Model

data class VerifyOtpRequest(
    val email: String,
    val code: String
)
package com.example.educationalpractice.Data.Model

data class VerifyRequest(
    val token: String,
    val email: String,
    val type: String = "signup"
)

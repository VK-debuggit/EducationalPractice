package com.example.educationalpractice.Data.Model

data class VerifyRequest(
    val type: String = "email",
    val token: String,
    val email: String
)

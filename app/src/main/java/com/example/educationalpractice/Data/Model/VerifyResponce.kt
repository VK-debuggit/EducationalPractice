package com.example.educationalpractice.Data.Model

data class VerifyResponse(
    val access_token: String? = null,
    val token_type: String? = null,
    val expires_in: Int? = null,
    val user: User? = null
)
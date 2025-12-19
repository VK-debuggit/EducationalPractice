package com.example.educationalpractice.data.model

data class UpdateUserRequest(
    val email: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val data: Map<String, Any>? = null // Для дополнительных данных
)
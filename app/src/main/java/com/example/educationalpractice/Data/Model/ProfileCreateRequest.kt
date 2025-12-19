package com.example.educationalpractice.Data.Model

data class ProfileCreateRequest(
    val id: String,
    val user_id: String,
    val firstname: String,
    val email: String
)
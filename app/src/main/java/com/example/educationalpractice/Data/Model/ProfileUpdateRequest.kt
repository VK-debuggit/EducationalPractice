package com.example.educationalpractice.Data.Model

data class ProfileUpdateRequest(
    val firstname: String? = null,
    val lastname: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val photo: String? = null
)
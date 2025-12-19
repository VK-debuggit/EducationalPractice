package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    val email: String,
    val token: String,
    val type: String = "email"
)
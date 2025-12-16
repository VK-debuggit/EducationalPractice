package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    val access_token: String? = null,
    val token_type: String? = null,
    val expires_in: Int? = null,
    val refresh_token: String? = null,
    val user: User? = null
)


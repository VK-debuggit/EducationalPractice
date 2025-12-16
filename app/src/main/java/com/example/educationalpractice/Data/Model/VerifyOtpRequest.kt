package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("token")
    val token: String,

    @SerializedName("type")
    val type: String = "signup"
)

data class VerifyOtpSimpleRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("token")
    val token: String,

    @SerializedName("type")
    val type: String = "email"
)
package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("access_token") val access_token: String? = null,
    @SerializedName("token_type") val token_type: String? = null,
    @SerializedName("expires_in") val expires_in: Int? = null,
    @SerializedName("refresh_token") val refresh_token: String? = null,
    @SerializedName("user") val user: Any? = null
)

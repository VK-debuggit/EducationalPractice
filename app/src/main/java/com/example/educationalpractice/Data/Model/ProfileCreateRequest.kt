// ProfileCreateRequest.kt
package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

data class ProfileCreateRequest(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("email") val email: String? = null
)
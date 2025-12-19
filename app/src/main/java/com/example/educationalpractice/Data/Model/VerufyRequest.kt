// VerifyRequest.kt
package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

//data class VerifyRequest(
//    @SerializedName("token") val token: String,
//    @SerializedName("email") val email: String
//)

data class VerifyRequest(
    val type: String = "email",
    val token: String,
    val email: String
)
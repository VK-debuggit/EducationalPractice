package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

//data class VerifyResponse(
//    @SerializedName("access_token") val accessToken: String? = null,
//    @SerializedName("token_type") val tokenType: String? = null,
//    @SerializedName("expires_in") val expiresIn: Int? = null,
//    @SerializedName("refresh_token") val refreshToken: String? = null,
//    @SerializedName("user") val user: User? = null
//)

//data class VerifyResponse(
//    val access_token: String? = null,
//    val user: Any? = null
//)

data class VerifyResponse(
    val user: VerifyUser? = null,
    val message: String? = null
)

data class VerifyUser(
    val id: String? = null,
    val email: String? = null,
    val confirmed_at: String? = null
)
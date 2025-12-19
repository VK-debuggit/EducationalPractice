//package com.example.educationalpractice.Data.Model
//
//data class SignUpResponse(
//    val id: String? = null,
//    val email: String? = null
//)

package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("user") val user: User? = null,
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null
)

data class User(
    @SerializedName("id") val id: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("confirmed_at") val confirmedAt: String? = null
)
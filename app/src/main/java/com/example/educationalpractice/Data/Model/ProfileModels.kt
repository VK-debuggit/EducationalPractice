package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

//data class ProfileCreateRequest(
//    @SerializedName("id") val id: String,
//    @SerializedName("user_id") val userId: String,
//    @SerializedName("firstname") val firstname: String,
//    @SerializedName("email") val email: String? = null,
//    @SerializedName("lastname") val lastname: String? = null,
//    @SerializedName("address") val address: String? = null,
//    @SerializedName("phone") val phone: String? = null,
//    @SerializedName("photo") val photo: String? = null
//)

data class ProfileUpdateRequest(
    @SerializedName("firstname") val firstname: String? = null,
    @SerializedName("lastname") val lastname: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("photo") val photo: String? = null
)

data class ProfileResponse(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("email") val email: String?,
    @SerializedName("lastname") val lastname: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("created_at") val createdAt: String
)
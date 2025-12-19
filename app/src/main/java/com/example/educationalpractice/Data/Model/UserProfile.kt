package com.example.shoestore.data.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("lastname") val lastname: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("photo") val photo: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)
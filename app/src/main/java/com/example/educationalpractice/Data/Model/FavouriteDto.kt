package com.example.shoestore.data.model

import com.google.gson.annotations.SerializedName

data class FavouriteDto(
    val id: String,
    val user_id: String,
    val product_id: String,
    val created_at: String?
)
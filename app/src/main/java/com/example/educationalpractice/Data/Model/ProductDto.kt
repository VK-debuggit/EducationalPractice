package com.example.shoestore.data.model

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val description: String?,
    val image_url: String?,
    val category: String?,
    val created_at: String?
)
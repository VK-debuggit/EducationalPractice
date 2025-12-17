// Data/Models/ProductModel.kt
package com.example.educationalpractice.Data.Models

data class ProductItem(
    val id: Int,
    val name: String,
    val price: String,
    val imageResId: Int,
    val isBestSeller: Boolean = false
)
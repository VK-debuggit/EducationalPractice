// Data/Models/Favorite.kt
package com.example.educationalpractice.Data.Models

import java.util.UUID

data class Favorite(
    val id: String,
    val product_id: String,
    val user_id: String
)

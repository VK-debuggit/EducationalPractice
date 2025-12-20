package com.example.educationalpractice.Data.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("id")
    val id: String,

    @SerialName("title")
    val title: String,

    @SerialName("cost")
    val cost: Double,

    @SerialName("description")
    val description: String,

    @SerialName("is_best_seller")
    val is_best_seller: Boolean? = false,

    @SerialName("category_id")
    val category_id: String? = null
)
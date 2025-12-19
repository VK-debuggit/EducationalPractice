//package com.example.educationalpractice.Data.Model
//
//data class SignUpResponse(
//    val id: String? = null,
//    val email: String? = null
//)

package com.example.educationalpractice.Data.Model

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    val id: String,
    val email: String? = null,
    val created_at: String? = null,
    val confirmed_at: String? = null
)
package com.example.shoestore.data

object TokenStorage {
    var accessToken: String? = null
        private set

    fun saveToken(token: String) {
        accessToken = token
    }

    fun clearToken() {
        accessToken = null
    }
}
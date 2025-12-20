// Data/Repository/FavoriteRepository.kt
package com.example.educationalpractice.Data.Repository

import android.util.Log
import com.example.educationalpractice.Data.Models.Favorite
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.result.PostgrestResult
import java.util.UUID

class FavoriteRepository(private val supabaseClient: SupabaseClient) {

    private val tableName = "favourite"

    suspend fun getFavoritesForUser(userId: String): List<Favorite> {
        return try {
            val result: PostgrestResult = supabaseClient.postgrest[tableName]
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                    order("id", Order.ASCENDING)
                }

            result.decodeList<Favorite>()
        } catch (e: Exception) {
            Log.e("FavoriteRepository", "Ошибка при получении избранного для пользователя $userId", e)
            emptyList()
        }
    }

    suspend fun addFavorite(productId: String, userId: String): Boolean {
        return try {
            val newFavorite = Favorite(
                id = UUID.randomUUID().toString(), // ← ВАЖНО: .toString()
                product_id = productId,
                user_id = userId
            )

            supabaseClient.postgrest[tableName].insert(newFavorite)
            true

        } catch (e: Exception) {
            Log.e("FavoriteRepository", "Ошибка при добавлении в избранное productId: $productId", e)
            false
        }
    }

    suspend fun removeFavorite(productId: String, userId: String): Boolean {
        return try {
            supabaseClient.postgrest[tableName]
                .delete {
                    filter {
                        eq("product_id", productId)
                        eq("user_id", userId)
                    }
                }
            true

        } catch (e: Exception) {
            Log.e("FavoriteRepository", "Ошибка при удалении из избранного productId: $productId", e)
            false
        }
    }
}
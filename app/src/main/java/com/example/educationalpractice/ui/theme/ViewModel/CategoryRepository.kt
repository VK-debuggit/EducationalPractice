package com.example.educationalpractice.Data.Repository

import android.util.Log
import com.example.educationalpractice.Category
import com.example.educationalpractice.Data.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository {

    suspend fun getAllCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            Log.d("CategoryRepository", "▶️ Загружаем категории...")

            // ПРОСТОЙ ЗАПРОС, КОТОРЫЙ РАБОТАЕТ
            val categories = SupabaseClient.client
                .from("categories")
                .select()
                .decodeList<Category>()

            Log.d("CategoryRepository", "✅ УСПЕХ! Категорий: ${categories.size}")

            categories.forEachIndexed { i, category ->
                Log.d("CategoryRepository", "📋 $i: ${category.title} (${category.id})")
            }

            return@withContext categories

        } catch (e: Exception) {
            Log.e("CategoryRepository", "❌ ОШИБКА: ${e.message}")
            Log.e("CategoryRepository", "Тип ошибки: ${e.javaClass.simpleName}")
            return@withContext emptyList()
        }
    }
}
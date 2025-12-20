package com.example.educationalpractice

import android.util.Log
import com.example.educationalpractice.Data.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val title: String
)

object CategoryLoader {

    suspend fun fetchAndLogCategories() {
        withContext(Dispatchers.IO) {
            try {
                Log.d("CategoryLoader", "🚀 НАЧИНАЕМ ЗАГРУЗКУ КАТЕГОРИЙ...")

                // Получаем все строки из таблицы Categories
                val categories = SupabaseClient.client
                    .from("categories")  // Имя таблицы в БД
                    .select()
                    .decodeList<Category>()

                Log.d("CategoryLoader", "✅ УСПЕХ! Загружено категорий: ${categories.size}")

                if (categories.isEmpty()) {
                    Log.d("CategoryLoader", "⚠️ Таблица 'categories' ПУСТАЯ!")
                    Log.d("CategoryLoader", "ℹ️ Проверьте в Supabase:")
                    Log.d("CategoryLoader", "   1. Table Editor → categories")
                    Log.d("CategoryLoader", "   2. Добавьте данные командой:")
                    Log.d("CategoryLoader", "      INSERT INTO categories (id, title) VALUES ('uuid-here', 'Название')")
                    Log.d("CategoryLoader", "   3. Проверьте RLS политики")
                } else {
                    // Выводим КАЖДОЕ наименование в логи
                    Log.d("CategoryLoader", "📊 СПИСОК ВСЕХ КАТЕГОРИЙ:")
                    for ((index, category) in categories.withIndex()) {
                        Log.d("CategoryLoader", "   ${index + 1}. ${category.title} (ID: ${category.id})")
                    }

                    // Также выводим только названия
                    Log.d("CategoryLoader", "🏷️ ТОЛЬКО НАЗВАНИЯ:")
                    categories.forEach { category ->
                        Log.d("CategoryLoader", "   • ${category.title}")
                    }
                }

            } catch (e: Exception) {
                Log.e("CategoryLoader", "❌ ОШИБКА ПРИ ПОЛУЧЕНИИ ДАННЫХ:", e)
                Log.e("CategoryLoader", "   Сообщение: ${e.message}")
                Log.e("CategoryLoader", "   Тип ошибки: ${e.javaClass.simpleName}")
            }
        }
    }
}
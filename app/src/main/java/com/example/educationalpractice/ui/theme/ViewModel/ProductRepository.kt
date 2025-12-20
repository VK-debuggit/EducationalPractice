package com.example.educationalpractice.Data.Repository

import android.util.Log
import com.example.educationalpractice.Data.Models.Product
import com.example.educationalpractice.Data.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository {

    suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            Log.d("ProductRepository", "Загружаем все товары...")

            val products = SupabaseClient.client
                .from("products")
                .select()
                .decodeList<Product>()

            Log.d("ProductRepository", "✅ Успех! Товаров: ${products.size}")

            // ДЛЯ ОТЛАДКИ: покажем статус бестселлера для первых товаров
            products.take(3).forEachIndexed { index, product ->
                Log.d("ProductRepository", "Товар ${index + 1}: ${product.title}, is_best_seller: ${product.is_best_seller}")
            }

            return@withContext products

        } catch (e: Exception) {
            Log.e("ProductRepository", "❌ Ошибка: ${e.message}")
            return@withContext emptyList()
        }
    }

    suspend fun getBestSellerProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            Log.d("ProductRepository", "Ищем бестселлеры...")

            val allProducts = getAllProducts()

            // Фильтруем по новому полю is_best_seller
            val bestSellers = allProducts.filter { product ->
                product.is_best_seller == true
            }

            Log.d("ProductRepository", "✅ Бестселлеров (is_best_seller=true): ${bestSellers.size}")
            return@withContext bestSellers.take(10)

        } catch (e: Exception) {
            Log.e("ProductRepository", "❌ Ошибка: ${e.message}")
            return@withContext emptyList()
        }
    }

    suspend fun getProductsByCategory(categoryId: String): List<Product> = withContext(Dispatchers.IO) {
        try {
            Log.d("ProductRepository", "Ищем товары для категории $categoryId...")

            val allProducts = getAllProducts()

            val filteredProducts = allProducts.filter { product ->
                product.category_id == categoryId
            }

            Log.d("ProductRepository", "✅ Товаров в категории: ${filteredProducts.size}")
            return@withContext filteredProducts

        } catch (e: Exception) {
            Log.e("ProductRepository", "❌ Ошибка: ${e.message}")
            return@withContext emptyList()
        }
    }
}
// Data/Screens/FavoriteScreen.kt
package com.example.educationalpractice.Data.Screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educationalpractice.Data.Components.ProductCard
import com.example.educationalpractice.Data.Models.ProductItem
import com.example.educationalpractice.Data.Repository.FavoriteRepository
import com.example.educationalpractice.Data.Repository.ProductRepository
import com.example.educationalpractice.Data.SupabaseClient
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.ViewModel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current

    // Получаем userId через ViewModel профиля
    val userId = remember {
        profileViewModel.getUserIdFromSharedPreferences(context) ?: ""
    }

    if (userId.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Инициализация пользователя...")
            }
        }
        return
    }

    val favoriteRepository = remember { FavoriteRepository(SupabaseClient.client) }
    val productRepository = remember { ProductRepository() }
    val coroutineScope = rememberCoroutineScope()

    // Состояние списка избранных товаров
    var favoriteProducts by remember { mutableStateOf<List<ProductItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Функция для загрузки данных из БД
    val loadFavorites: () -> Unit = {
        coroutineScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // 1. Получаем ID избранных товаров
                val favoriteEntries = favoriteRepository.getFavoritesForUser(userId)
                val favoriteProductIds = favoriteEntries.map { it.product_id }

                Log.d("FavoriteScreen", "Найдено избранных ID: ${favoriteProductIds.size}")

                if (favoriteProductIds.isEmpty()) {
                    favoriteProducts = emptyList()
                } else {
                    // 2. Получаем все продукты
                    val allProducts = productRepository.getAllProducts()

                    // 3. Фильтруем только избранные
                    val filteredProducts = allProducts.filter { product ->
                        favoriteProductIds.contains(product.id)
                    }

                    // 4. Конвертируем в ProductItem
                    favoriteProducts = filteredProducts.map { product ->
                        ProductItem(
                            id = product.id,
                            name = product.title,
                            price = "P${"%.2f".format(product.cost)}",
                            imageResId = R.drawable.cross,
                            isBestSeller = product.is_best_seller == true
                        )
                    }

                    Log.d("FavoriteScreen", "Загружено товаров: ${favoriteProducts.size}")
                }

            } catch (e: Exception) {
                errorMessage = "Не удалось загрузить избранные товары: ${e.message}"
                Log.e("FavoriteScreen", "Error loading favorites", e)
                favoriteProducts = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    // Загружаем избранное при первом показе экрана
    LaunchedEffect(Unit) {
        loadFavorites()
    }

    // Обработчик нажатия на сердечко
    val onFavoriteClick: (ProductItem) -> Unit = { product ->
        coroutineScope.launch {
            // Удаляем товар из избранного через репозиторий
            val success = favoriteRepository.removeFavorite(product.id, userId)
            if (success) {
                // Если успешно, обновляем локальный список, чтобы UI обновился
                favoriteProducts = favoriteProducts.filter { it.id != product.id }
                Log.d("FavoriteScreen", "Товар удален: ${product.id}")
            } else {
                // Обработка ошибки удаления
                Log.e("FavoriteScreen", "Failed to remove favorite: ${product.id}")
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Заголовок экрана
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Избранное",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = Accent)
                }
            }
            errorMessage != null -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(16.dp))
                }
            }
            favoriteProducts.isEmpty() -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Список избранного пуст", fontSize = 18.sp)
                        Text(text = "Добавляйте товары, нажимая на ❤️",
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
            else -> {
                // Отображаем товары в виде сетки
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(favoriteProducts, key = { it.id }) { product ->
                        ProductCard(
                            modifier = Modifier.fillMaxWidth(),
                            productImageResId = product.imageResId,
                            productName = product.name,
                            productPrice = product.price,
                            badgeText = if (product.isBestSeller) "BEST SELLER" else "",
                            isFavorite = true, // На экране избранного все карточки избранные
                            onCardClick = { /* Логика перехода на детальный экран */ },
                            onFavoriteClick = { onFavoriteClick(product) } // Обработчик удаления
                        )
                    }
                }
            }
        }
    }
}
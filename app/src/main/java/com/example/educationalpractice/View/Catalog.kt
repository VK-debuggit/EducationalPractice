package com.example.educationalpractice.View

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.educationalpractice.Data.Components.ProductCard
import com.example.educationalpractice.Data.Models.Product
import com.example.educationalpractice.Data.Models.ProductItem
import com.example.educationalpractice.Data.Repository.CategoryRepository
import com.example.educationalpractice.Data.Repository.FavoriteRepository
import com.example.educationalpractice.Data.Repository.ProductRepository
import com.example.educationalpractice.Data.Screens.CategoryChip
import com.example.educationalpractice.Data.SupabaseClient
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.*
import com.example.educationalpractice.ui.theme.ViewModel.ProfileViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun Catalog(
    navController: NavController,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current

    // Получаем userId через ViewModel профиля - исправлено
    val userIdState = remember { mutableStateOf("") }

    // Загружаем userId
    LaunchedEffect(Unit) {
        var userId = profileViewModel.getUserIdFromSharedPreferences(context) ?: ""

        if (userId.isEmpty()) {
            userId = UUID.randomUUID().toString()
            val authPrefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            authPrefs.edit().putString("user_id", userId).apply()
            val userPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            userPrefs.edit().putString("user_id", userId).apply()
        }

        userIdState.value = userId
        Log.d("Catalog", "User ID: ${userIdState.value}")
    }

    // Состояния для данных из БД
    val allCategories = remember { mutableStateOf<List<com.example.educationalpractice.Category>>(emptyList()) }
    val products = remember { mutableStateOf<List<ProductItem>>(emptyList()) }
    val favoriteRepository = remember { FavoriteRepository(SupabaseClient.client) }
    val favoriteProductIds = remember { mutableStateOf<Set<String>>(emptySet()) }
    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Текущие выбранные значения
    val selectedCategory = remember { mutableStateOf("Все") }
    val selectedCategoryId = remember { mutableStateOf<String?>(null) }

    // Формирование списка для отображения
    val displayCategories = remember(allCategories.value) {
        listOf("Все" to null) + allCategories.value.map { it.title to it.id }
    }

    // Функция клика
    val onCategorySelected: (String, String?) -> Unit = { title, id ->
        selectedCategory.value = title
        selectedCategoryId.value = id
        Log.d("CATALOG", "Выбрано: $title, ID: $id")
        coroutineScope.launch {
            loadProducts(title, id, products, isLoading, errorMessage)
        }
    }

    // Загружаем категории
    LaunchedEffect(Unit) {
        try {
            val categoryRepo = CategoryRepository()
            val dbCategories = categoryRepo.getAllCategories()
            allCategories.value = dbCategories
            Log.d("Catalog", "Загружено категорий: ${dbCategories.size}")
        } catch (e: Exception) {
            Log.e("Catalog", "Ошибка загрузки категорий", e)
        }
    }

    // Загружаем избранные товары пользователя
    LaunchedEffect(userIdState.value) {
        if (userIdState.value.isNotEmpty()) {
            try {
                val favorites = favoriteRepository.getFavoritesForUser(userIdState.value) // Используем .value
                favoriteProductIds.value = favorites.map { it.product_id }.toSet()
                Log.d("Catalog", "Загружено избранных: ${favoriteProductIds.value.size}")
            } catch (e: Exception) {
                Log.e("Catalog", "Ошибка загрузки избранного", e)
            }
        }
    }

    // Первичная загрузка товаров
    LaunchedEffect(Unit) {
        loadProducts("Все", null, products, isLoading, errorMessage)
    }

    // Функция для переключения избранного - исправлено
    val onFavoriteToggle: (String, Boolean) -> Unit = { productId, isFavorite ->
        coroutineScope.launch {
            try {
                if (isFavorite) {
                    val success = favoriteRepository.addFavorite(productId, userIdState.value) // Используем .value
                    if (success) {
                        favoriteProductIds.value = favoriteProductIds.value + productId
                        Log.d("Catalog", "Товар добавлен в избранное: $productId")
                    }
                } else {
                    val success = favoriteRepository.removeFavorite(productId, userIdState.value) // Используем .value
                    if (success) {
                        favoriteProductIds.value = favoriteProductIds.value - productId
                        Log.d("Catalog", "Товар удален из избранного: $productId")
                    }
                }
            } catch (e: Exception) {
                Log.e("Catalog", "Ошибка при изменении избранного", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.iconback),
                    contentDescription = "Назад",
                    tint = Text
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = selectedCategory.value,
                style = Typography.headlineSmall,
                color = Text,
                modifier = Modifier.weight(1f)
            )
        }

        // Категории
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Категории",
                style = Typography.headlineSmall,
                color = Text,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(displayCategories) { item: Pair<String, String?> ->
                    CategoryChip(
                        text = item.first,
                        isSelected = item.first == selectedCategory.value,
                        onClick = { onCategorySelected(item.first, item.second) }
                    )
                }
            }
        }

        // Список товаров
        if (isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Accent)
            }
        } else if (errorMessage.value != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ошибка", color = Color.Red)
                    Text(errorMessage.value ?: "", color = Hint)
                }
            }
        } else if (products.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Товары не найдены", color = Hint)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Категория: ${selectedCategory.value}",
                        color = Hint,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products.value.chunked(2)) { rowProducts: List<ProductItem> ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Первый товар в строке
                        ProductCard(
                            modifier = Modifier.weight(1f),
                            productImageResId = rowProducts[0].imageResId,
                            badgeText = if (rowProducts[0].isBestSeller) "BEST SELLER" else "",
                            productName = rowProducts[0].name,
                            productPrice = rowProducts[0].price,
                            isFavorite = favoriteProductIds.value.contains(rowProducts[0].id),
                            onFavoriteClick = {
                                val newFavoriteState = !favoriteProductIds.value.contains(rowProducts[0].id)
                                onFavoriteToggle(rowProducts[0].id, newFavoriteState)
                            }
                        )

                        // Второй товар в строке
                        if (rowProducts.size > 1) {
                            ProductCard(
                                modifier = Modifier.weight(1f),
                                productImageResId = rowProducts[1].imageResId,
                                badgeText = if (rowProducts[1].isBestSeller) "BEST SELLER" else "",
                                productName = rowProducts[1].name,
                                productPrice = rowProducts[1].price,
                                isFavorite = favoriteProductIds.value.contains(rowProducts[1].id),
                                onFavoriteClick = {
                                    val newFavoriteState = !favoriteProductIds.value.contains(rowProducts[1].id)
                                    onFavoriteToggle(rowProducts[1].id, newFavoriteState)
                                }
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// Вынесенная функция загрузки товаров
private suspend fun loadProducts(
    categoryName: String,
    categoryId: String?,
    productsState: MutableState<List<ProductItem>>,
    isLoadingState: MutableState<Boolean>,
    errorState: MutableState<String?>
) {
    isLoadingState.value = true
    errorState.value = null

    try {
        Log.d("Catalog", "Загружаем товары для категории: '$categoryName', ID: '$categoryId'")

        val productRepo = ProductRepository()
        val dbProducts: List<Product> = if (categoryName == "Все" || categoryId == null) {
            productRepo.getAllProducts()
        } else {
            productRepo.getProductsByCategory(categoryId)
        }

        if (dbProducts.isNotEmpty()) {
            productsState.value = dbProducts.map { product ->
                ProductItem(
                    id = product.id,
                    name = product.title,
                    price = "P${"%.2f".format(product.cost)}",
                    imageResId = R.drawable.cross,
                    isBestSeller = product.is_best_seller == true
                )
            }
            Log.d("Catalog", "✅ Загружено товаров: ${productsState.value.size}")
        } else {
            productsState.value = emptyList()
            Log.d("Catalog", "⚠️ Товары не найдены")
        }

    } catch (e: Exception) {
        Log.e("Catalog", "❌ Ошибка загрузки данных", e)
        errorState.value = "Ошибка: ${e.message}"
        productsState.value = emptyList()
    } finally {
        isLoadingState.value = false
    }
}
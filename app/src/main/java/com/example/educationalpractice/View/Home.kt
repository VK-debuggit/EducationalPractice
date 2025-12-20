// Data/Screens/HomeScreen.kt
package com.example.educationalpractice.Data.Screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educationalpractice.Category
import com.example.educationalpractice.Data.Components.ProductCard
import com.example.educationalpractice.Data.Models.Product
import com.example.educationalpractice.Data.Models.ProductItem
import com.example.educationalpractice.Data.Repository.CategoryRepository
import com.example.educationalpractice.Data.Repository.FavoriteRepository
import com.example.educationalpractice.Data.Repository.ProductRepository
import com.example.educationalpractice.Data.SupabaseClient
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.*
import com.example.educationalpractice.ui.theme.ViewModel.ProfileViewModel
import com.yourpackage.ui.components.BottomNavigationComponent
import kotlinx.coroutines.launch
import java.util.UUID

// HomeScreen.kt - верните старый простой код без избранного для начала:
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    // Состояние для категорий и товаров
    val selectedCategory = remember { mutableStateOf("Все") }
    val categories = remember { mutableStateOf<List<Category>>(emptyList()) }
    val bestSellerProducts = remember { mutableStateOf<List<ProductItem>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // Загружаем данные при старте
    LaunchedEffect(Unit) {
        isLoading.value = true
        errorMessage.value = null

        try {
            Log.d("HomeScreen", "🚀 Начинаем загрузку данных...")

            // 1. Загружаем категории из БД
            val categoryRepo = CategoryRepository()
            val dbCategories: List<Category> = categoryRepo.getAllCategories()
            Log.d("HomeScreen", "📊 Категорий загружено: ${dbCategories.size}")
            categories.value = dbCategories

            // 2. Загружаем бестселлеры из БД
            val productRepo = ProductRepository()
            val dbBestSellers: List<Product> = productRepo.getBestSellerProducts()
            Log.d("HomeScreen", "🛒 Бестселлеров загружено: ${dbBestSellers.size}")

            if (dbBestSellers.isNotEmpty()) {
                bestSellerProducts.value = dbBestSellers.map { product: Product ->
                    ProductItem(
                        id = product.id,
                        name = product.title,
                        price = "P${"%.2f".format(product.cost)}",
                        imageResId = R.drawable.cross,
                        isBestSeller = product.is_best_seller == true
                    )
                }
            } else {
                Log.d("HomeScreen", "⚠️ Бестселлеры не найдены в БД")
                bestSellerProducts.value = getTestProducts()
            }

        } catch (e: Exception) {
            Log.e("HomeScreen", "❌ Ошибка загрузки данных", e)
            errorMessage.value = "Ошибка: ${e.message}"
            bestSellerProducts.value = getTestProducts()

        } finally {
            isLoading.value = false
        }
    }

    // Используем bestSellerProducts
    val popularProducts = bestSellerProducts.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.Explore),
                            style = Typography.displayMedium,
                            color = Text
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationComponent(
                homeIcon = R.drawable.home,
                favoriteIcon = R.drawable.favorite,
                bagIcon = R.drawable.bag_2,
                ordersIcon = R.drawable.orders,
                profileIcon = R.drawable.profile,
                initialSelectedItem = R.drawable.home,
                onItemSelected = { selectedIcon ->
                    when (selectedIcon) {
                        R.drawable.home -> NavigationManager.navigateTo(Views.Home.route)
                        R.drawable.favorite -> NavigationManager.navigateTo(Views.Favorite.route)
                        R.drawable.bag_2 -> {}
                        R.drawable.orders -> {}
                        R.drawable.profile -> NavigationManager.navigateTo(Views.Profile.route)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // Поисковая строка
            SearchBar(modifier = Modifier.padding(16.dp))

            // Категории
            Text(
                text = stringResource(R.string.Select),
                style = Typography.headlineSmall,
                color = Text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Accent)
                }
            } else if (errorMessage.value != null) {
                Text(
                    text = errorMessage.value ?: "Ошибка",
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                ShowCategories(
                    categories = categories.value,
                    selectedCategory = selectedCategory.value,
                    onCategorySelected = { categoryTitle, categoryId ->
                        selectedCategory.value = categoryTitle
                        NavigationManager.navigateTo(Views.Catalog.withCategory(categoryTitle, categoryId))
                    }
                )
            } else if (categories.value.isEmpty()) {
                Text(
                    text = "Категории не найдены",
                    color = Hint,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                ShowCategories(
                    categories = categories.value,
                    selectedCategory = selectedCategory.value,
                    onCategorySelected = { categoryTitle, categoryId ->
                        selectedCategory.value = categoryTitle
                        NavigationManager.navigateTo(Views.Catalog.withCategory(categoryTitle, categoryId))
                    }
                )
            }

            // Популярные товары БЕЗ избранного (простая версия)
            PopularSectionSimple(
                products = popularProducts,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Акции
            PromoSectionWithImage(modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// Простая версия PopularSection без избранного
@Composable
fun PopularSectionSimple(
    products: List<ProductItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Popular),
                style = Typography.headlineSmall,
                color = Text
            )

            Text(
                text = stringResource(R.string.See),
                style = Typography.displaySmall,
                color = Accent,
                modifier = Modifier.clickable {
                    NavigationManager.navigateTo(Views.Catalog.withCategory("Все", null))
                }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products, key = { it.id }) { product: ProductItem ->
                ProductCard(
                    modifier = Modifier.width(160.dp),
                    productImageResId = product.imageResId,
                    badgeText = if (product.isBestSeller) "BEST SELLER" else "",
                    productName = product.name,
                    productPrice = product.price,
                    isFavorite = false,
                    onFavoriteClick = {} // Пустая функция пока
                )
            }
        }
    }
}

@Composable
fun ShowCategories(
    categories: List<Category>,
    selectedCategory: String,
    onCategorySelected: (String, String?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        // Сначала добавляем "Все"
        item {
            CategoryChip(
                text = "Все",
                isSelected = "Все" == selectedCategory,
                onClick = {
                    onCategorySelected("Все", null)
                }
            )
        }

        // Потом все остальные категории
        items(categories) { category ->
            CategoryChip(
                text = category.title,
                isSelected = category.title == selectedCategory,
                onClick = {
                    onCategorySelected(category.title, category.id)
                }
            )
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (isSelected) Accent else Color(0xFFF5F5F5)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Text,
            fontSize = 14.sp
        )
    }
}

@Composable
fun PopularSection(
    products: List<ProductItem>,
    favoriteProductIds: Set<String>,
    userId: String,
    onFavoriteToggle: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Popular),
                style = Typography.headlineSmall,
                color = Text
            )

            Text(
                text = stringResource(R.string.See),
                style = Typography.displaySmall,
                color = Accent,
                modifier = Modifier.clickable {
                    NavigationManager.navigateTo(Views.Catalog.withCategory("Все", null))
                }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products, key = { it.id }) { product: ProductItem ->
                ProductCard(
                    modifier = Modifier.width(160.dp),
                    productImageResId = product.imageResId,
                    badgeText = if (product.isBestSeller) "BEST SELLER" else "",
                    productName = product.name,
                    productPrice = product.price,
                    isFavorite = favoriteProductIds.contains(product.id),
                    onFavoriteClick = {
                        val newFavoriteState = !favoriteProductIds.contains(product.id)
                        onFavoriteToggle(product.id, newFavoriteState)
                    }
                )
            }
        }
    }
}

@Composable
fun PromoSectionWithImage(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.New),
                style = Typography.headlineSmall,
                color = Text
            )

            Text(
                text = stringResource(R.string.See),
                style = Typography.displaySmall,
                color = Accent,
                modifier = Modifier.clickable {
                    NavigationManager.navigateTo(Views.Catalog.withCategory("Все", null))
                }
            )
        }

        // Картинка вместо текста
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    NavigationManager.navigateTo(Views.Catalog.withCategory("Все", null))
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.frame),
                contentDescription = "Рекламный баннер",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .background(
                    color = Block,
                    shape = RoundedCornerShape(25.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.find),
                    contentDescription = "Поиск",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.Looking),
                    color = Hint,
                    fontSize = 16.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = Accent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sliders),
                contentDescription = "Фильтры",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

fun getTestProducts(): List<ProductItem> = listOf(
    ProductItem(
        id = "1",
        name = "Nike Air Max",
        price = "P750.00",
        imageResId = R.drawable.cross,
        isBestSeller = true
    ),
    ProductItem(
        id = "2",
        name = "Nike Air Force",
        price = "P650.00",
        imageResId = R.drawable.cross,
        isBestSeller = true
    ),
    ProductItem(
        id = "3",
        name = "Adidas Superstar",
        price = "P800.00",
        imageResId = R.drawable.cross,
        isBestSeller = false
    )
)
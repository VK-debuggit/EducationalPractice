// Data/Screens/Home.kt
package com.example.educationalpractice.Data.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educationalpractice.Data.Components.ProductCard
import com.example.educationalpractice.Data.Models.ProductItem
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Background
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.ui.theme.Hint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier) {
    val selectedCategory = remember { mutableStateOf("Все") }
    val categories = listOf("Все", "Outdoor", "Tennis")

    val popularProducts = listOf(
        ProductItem(
            id = 1,
            name = "Nike Air Max",
            price = "P750.00",
            imageResId = R.drawable.cross,
            isBestSeller = true
        ),
        ProductItem(
            id = 2,
            name = "Nike Air Max",
            price = "P750.00",
            imageResId = R.drawable.cross,
            isBestSeller = true
        ),
        ProductItem(
            id = 3,
            name = "Nike Air Max",
            price = "P750.00",
            imageResId = R.drawable.cross,
            isBestSeller = true
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Заголовок
                        Text(
                            text = stringResource(R.string.Explore),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        // Пустой элемент для баланса
                        Spacer(modifier = Modifier.width(60.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigation()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Поисковая строка
            SearchBar(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

            // Категории
            CategoriesSection(
                categories = categories,
                selectedCategory = selectedCategory.value,
                onCategorySelected = { category ->
                    selectedCategory.value = category
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Популярное
            PopularSection(
                products = popularProducts,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Акции
            PromoSection(modifier = Modifier.padding(16.dp))
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
        // Поисковая строка (белая)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(25.dp)
                )
                .clickable {},
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

        // Иконка настроек на синем круге
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = Accent,
                    shape = CircleShape
                )
                .clickable {
                    // Обработчик нажатия на иконку настроек
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.sliders),
                contentDescription = "Настройки",
                modifier = Modifier.size(24.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun CategoriesSection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Категории",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                CategoryChip(
                    text = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
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
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
fun PopularSection(
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
                text = "Популярное",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                text = "Все",
                fontSize = 14.sp,
                color = Accent,
                modifier = Modifier.clickable {
                    // Обработчик "Все"
                }
            )
        }

        LazyRow(
            modifier = Modifier.padding(top = 12.dp, start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    modifier = Modifier.width(160.dp),
                    productImageResId = product.imageResId,
                    badgeText = if (product.isBestSeller) "BEST SELLER" else "NEW",
                    productName = product.name,
                    productPrice = product.price,
                    onCardClick = {
                        // Обработчик клика на товар
                    }
                )
            }
        }
    }
}

@Composable
fun PromoSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Акции",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                text = "Все",
                fontSize = 14.sp,
                color = Accent,
                modifier = Modifier.clickable {}
            )
        }

        // Баннер акции
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.frame),
                    contentDescription = "Реклама",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                )
            }
        }
    }
}

@Composable
fun BottomNavigation() {
    val menuItems = listOf(
        R.drawable.home,
        R.drawable.favorite,
        R.drawable.orders,
        R.drawable.profile
    )
    val selectedIcon = remember { mutableStateOf(R.drawable.home) }

    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            menuItems.forEach { iconResId ->
                BottomNavIcon(
                    iconResId = iconResId,
                    isSelected = iconResId == selectedIcon.value,
                    onClick = { selectedIcon.value = iconResId }
                )
            }
        }
    }
}

@Composable
fun BottomNavIcon(
    iconResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = if (isSelected) {
                androidx.compose.ui.graphics.ColorFilter.tint(Accent)
            } else {
                androidx.compose.ui.graphics.ColorFilter.tint(Hint)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    EducationalPracticeTheme {
        HomeScreen(Modifier.background(Background))
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun HomeScreenFullPreview() {
    EducationalPracticeTheme {
        HomeScreen(Modifier.background(Background))
    }
}
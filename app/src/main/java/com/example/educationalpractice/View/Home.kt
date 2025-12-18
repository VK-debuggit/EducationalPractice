package com.example.educationalpractice.Data.Screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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
import com.example.educationalpractice.ui.theme.Block
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.ui.theme.Hint
import com.example.educationalpractice.ui.theme.Text
import com.example.educationalpractice.ui.theme.Typography

// ... ваш остальной код ...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier) {
    val selectedCategory = remember { mutableStateOf("Все") }
    val categories = listOf(stringResource(R.string.See), "Outdoor", "Tennis", "Men", "Women")

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
            BottomNavigationExactLikePicture()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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

                // Добавляем отступ снизу, чтобы контент не скрывался под меню
                Spacer(modifier = Modifier.height(100.dp))
            }
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
            text = stringResource(R.string.Select),
            style = Typography.headlineSmall,
            color = Text,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(categories) { category ->
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
            color = if (isSelected) Block else Text,
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
                text = stringResource(R.string.Popular),
                style = Typography.headlineSmall,
                color = Text
            )

            Text(
                text = stringResource(R.string.See),
                style = Typography.displaySmall,
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
                    badgeText = "BEST SELLER",
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
                text = stringResource(R.string.New),
                style = Typography.headlineSmall,
                color = Text
            )

            Text(
                text = stringResource(R.string.See),
                style = Typography.displaySmall,
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
fun BottomNavigationExactLikePicture() {
    val menuItems = listOf(
        R.drawable.home,
        R.drawable.favorite,
        R.drawable.bag_2,
        R.drawable.orders,
        R.drawable.profile
    )
    val selectedItem = remember { mutableStateOf(R.drawable.home) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp) // УВЕЛИЧИВАЕМ высоту
            .background(Color.White) // ← ДОБАВЛЕНО: белый фон для всего Box
    ) {
        // 1. СОЗДАЕМ КАСТОМНУЮ ФОРМУ С ВЫГНУТЫМ ВЕРХОМ
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomStart)
                .shadow(
                    elevation = 25.dp, // Увеличиваем тень
                    shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
                    clip = true
                )
        ) {
            val width = size.width
            val height = size.height
            val curveHeight = 25.dp.toPx()
            val centerButtonWidth = 70.dp.toPx()
            val centerNotchDepth = 50.dp.toPx()

            val path = Path().apply {
                moveTo(0f, height)
                lineTo(0f, curveHeight)
                quadraticBezierTo(0f, 0f, width * 0.15f, 0f)
                lineTo(width * 0.45f - centerButtonWidth / 2f, 0f)
                lineTo(width * 0.5f - centerButtonWidth / 3f, centerNotchDepth)
                lineTo(width * 0.5f + centerButtonWidth / 3f, centerNotchDepth)
                lineTo(width * 0.55f + centerButtonWidth / 2f, 0f)
                lineTo(width * 0.85f, 0f)
                quadraticBezierTo(width, 0f, width, curveHeight)
                lineTo(width, height)
                close()
            }

            // Рисуем белую заливку
            drawPath(
                path = path,
                color = Color.White,
                style = Fill
            )

            // Убираем обводку или делаем её белой
            drawPath(
                path = path,
                color = Color.White, // Меняем на белый
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // 2. РАСПОЛАГАЕМ ИКОНКИ НА ПАНЕЛИ
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Первые 2 иконки (Home, Favorite)
            menuItems.take(2).forEach { iconResId ->
                MenuIconItemCurved(
                    iconResId = iconResId,
                    isSelected = selectedItem.value == iconResId,
                    onClick = { selectedItem.value = iconResId }
                )
            }

            // Пустое место для центральной кнопки
            Spacer(modifier = Modifier.width(80.dp))

            // Последние 2 иконки (Orders, Profile)
            menuItems.drop(3).forEach { iconResId ->
                MenuIconItemCurved(
                    iconResId = iconResId,
                    isSelected = selectedItem.value == iconResId,
                    onClick = { selectedItem.value = iconResId }
                )
            }
        }

        // 3. ЦЕНТРАЛЬНАЯ КНОПКА В ПРОВАЛЕ (БОЛЬШЕ И ВЫШЕ)
        Box(
            modifier = Modifier
                .size(75.dp) // Увеличиваем размер
                .align(Alignment.TopCenter)
                .offset(y = 35.dp) // Поднимаем выше
                .shadow(
                    elevation = 25.dp, // Усиливаем тень
                    shape = CircleShape,
                    clip = false,
                    ambientColor = Accent.copy(alpha = 0.5f),
                    spotColor = Accent.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .background(Accent)
                .clickable { selectedItem.value = R.drawable.bag_2 },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bag_2),
                contentDescription = "Заказы",
                modifier = Modifier.size(36.dp), // Увеличиваем иконку
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun MenuIconItemCurved(
    iconResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(65.dp)
            .height(65.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                if (isSelected) Accent else Hint
            )
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
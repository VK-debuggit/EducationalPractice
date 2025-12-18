// Data/Components/ProductCard.kt
package com.example.educationalpractice.Data.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Block
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.ui.theme.Hint
import com.example.educationalpractice.ui.theme.Typography
import com.example.educationalpractice.ui.theme.Text as TextColor

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    productImageResId: Int = R.drawable.cross,
    badgeText: String = "BEST SELLER",
    productName: String = "Nike Air Max",
    productPrice: String = "P750.00",
    onCardClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .wrapContentHeight() // ← Заменяем фиксированную высоту на адаптивную
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight() // ← Карточка тоже адаптируется по высоте
                .clickable { onCardClick() },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(12.dp), // Увеличил паддинг для лучшего вида
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Иконка "избранное" в правом верхнем углу
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.favorite),
                        contentDescription = "Любимое",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = {})
                    )
                }

                // Изображение товара - теперь с aspectRatio
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f), // Сохраняем пропорции изображения
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = productImageResId),
                        contentDescription = productName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Text(
                    text = badgeText,
                    color = Accent,
                    style = Typography.displaySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Название товара с возможностью переноса
                Text(
                    text = productName,
                    color = Hint,
                    style = Typography.bodySmall,
                    lineHeight = 16.sp,
                    maxLines = 2, // Ограничиваем до 2 строк
                    overflow = TextOverflow.Ellipsis // Троеточие если не помещается
                )

                Text(
                    text = productPrice,
                    color = TextColor,
                    style = Typography.labelSmall
                )
            }
        }

        // Кнопка "+" в правом нижнем углу карточки
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomEnd)
                .background(
                    color = Accent,
                    shape = RoundedCornerShape(16.dp, 1.dp, 16.dp, 1.dp)
                )
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.add),
                contentDescription = "Добавить",
                modifier = Modifier.size(20.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    EducationalPracticeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            ProductCard()
        }
    }
}
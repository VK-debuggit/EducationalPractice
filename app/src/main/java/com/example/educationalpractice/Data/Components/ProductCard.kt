package com.example.educationalpractice.Data.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.*

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    productImageResId: Int = R.drawable.cross,
    badgeText: String = "",
    productName: String = "Nike Air Max",
    productPrice: String = "P750.00",
    isFavorite: Boolean = false,
    onCardClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Изображение товара с иконкой избранного
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                Image(
                    painter = painterResource(id = productImageResId),
                    contentDescription = productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                // Иконка избранного в углу
                Image(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.favorite_fill else R.drawable.favorite
                    ),
                    contentDescription = "Избранное",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                        .clickable { onFavoriteClick() },
                    colorFilter = if (isFavorite) ColorFilter.tint(Red) else null
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Бейдж BEST SELLER (если есть)
            if (badgeText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Accent.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = Accent,
                        style = Typography.displaySmall,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Название товара
            Text(
                text = productName,
                color = Text,
                style = Typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Цена
            Text(
                text = productPrice,
                color = Text,
                style = Typography.labelSmall
            )
        }
    }
}
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
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
    Card(
        modifier = modifier
            .width(160.dp)
            .height(220.dp)
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.favorite),
                contentDescription = "Любимое",
                modifier = Modifier
                    .clickable(
                        onClick = {}
                    )
            )
            // Изображение товара
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
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
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            // Название товара
            Text(
                text = productName,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp
            )

            Text(
                text = productPrice,
                color = TextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Accent,
                    shape = RoundedCornerShape(16.dp, 1.dp, 16.dp, 1.dp) // Углы скруглены на 8dp
                )
                .clickable {}
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
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
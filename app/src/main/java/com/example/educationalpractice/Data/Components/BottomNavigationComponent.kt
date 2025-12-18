// BottomNavigationComponent.kt (адаптированный из HomeScreen)
package com.yourpackage.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Hint

@Composable
fun BottomNavigationComponent(
    homeIcon: Int,
    favoriteIcon: Int,
    bagIcon: Int,
    ordersIcon: Int,
    profileIcon: Int,
    onItemSelected: (Int) -> Unit = {},
    initialSelectedItem: Int = homeIcon // Добавляем параметр для начального выбора
) {
    val menuItems = listOf(homeIcon, favoriteIcon, bagIcon, ordersIcon, profileIcon)
    val selectedItem = remember { mutableStateOf(initialSelectedItem) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // Изменено с 110 на 100 как в HomeScreen
            .background(Color.White)
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
            val centerButtonWidth = 80.dp.toPx() // Увеличено с 70
            val centerNotchDepth = 70.dp.toPx() // Увеличено с 50

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
                color = Color.White,
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
                    onClick = {
                        selectedItem.value = iconResId
                        onItemSelected(iconResId)
                    }
                )
            }

            // Пустое место для центральной кнопки
            Spacer(modifier = Modifier.width(80.dp))

            // Последние 2 иконки (Orders, Profile)
            menuItems.drop(3).forEach { iconResId ->
                MenuIconItemCurved(
                    iconResId = iconResId,
                    isSelected = selectedItem.value == iconResId,
                    onClick = {
                        selectedItem.value = iconResId
                        onItemSelected(iconResId)
                    }
                )
            }
        }

        // 3. ЦЕНТРАЛЬНАЯ КНОПКА В ПРОВАЛЕ (БОЛЬШЕ И ВЫШЕ)
        Box(
            modifier = Modifier
                .size(75.dp) // Увеличиваем размер
                .align(Alignment.TopCenter)
                .offset(y = -20.dp) // Поднимаем выше (изменено с 35 на -20)
                .shadow(
                    elevation = 50.dp, // Усиливаем тень (изменено с 25 на 50)
                    shape = CircleShape,
                    clip = false,
                    ambientColor = Accent.copy(alpha = 0.5f),
                    spotColor = Accent.copy(alpha = 0.5f)
                )
                .clip(CircleShape)
                .background(Accent)
                .clickable {
                    selectedItem.value = bagIcon
                    onItemSelected(bagIcon)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = bagIcon),
                contentDescription = "Заказы",
                modifier = Modifier.size(36.dp), // Увеличиваем иконку
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
private fun MenuIconItemCurved(
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
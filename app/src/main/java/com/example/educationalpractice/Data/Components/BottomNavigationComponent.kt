// BottomNavigationComponent.kt
package com.yourpackage.ui.components

import androidx.compose.foundation.*
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
    onItemSelected: (Int) -> Unit = {}
) {
    val menuItems = listOf(homeIcon, favoriteIcon, bagIcon, ordersIcon, profileIcon)
    val selectedItem = remember { mutableStateOf(homeIcon) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(Color.White)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomStart)
                .shadow(
                    elevation = 25.dp,
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

            drawPath(path = path, color = Color.White, style = Fill)
            drawPath(path = path, color = Color.White, style = Stroke(width = 1.dp.toPx()))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            Spacer(modifier = Modifier.width(80.dp))

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

        Box(
            modifier = Modifier
                .size(75.dp)
                .align(Alignment.TopCenter)
                .offset(y = 35.dp)
                .shadow(
                    elevation = 25.dp,
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
                modifier = Modifier.size(36.dp),
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
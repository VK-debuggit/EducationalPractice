package com.example.educationalpractice.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.ui.theme.Block
import com.example.educationalpractice.ui.theme.Disable

@Composable
fun PageIndicatorCustom(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Disable,
    inactiveColor: Color = Block,
    height: Int = 4,
    spacing: Int = 8
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.dp)
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            val width = if (isActive) 24.dp else 8.dp
            val color = if (isActive) activeColor else inactiveColor

            Box(
                modifier = Modifier
                    .width(width)
                    .height(height.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}
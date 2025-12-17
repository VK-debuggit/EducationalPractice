package com.example.educationalpractice.Data.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.ui.theme.Block
import com.example.educationalpractice.ui.theme.SubTextLight

@Composable
fun PageIndicatorSimple(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Block,
    inactiveColor: Color = SubTextLight.copy(alpha = 0.3f),
    indicatorHeight: Int = 4,
    activeIndicatorWidth: Int = 24,
    inactiveIndicatorWidth: Int = 8,
    spacing: Int = 8
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing.dp)
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            val width = if (isActive) activeIndicatorWidth.dp else inactiveIndicatorWidth.dp
            val color = if (isActive) activeColor else inactiveColor

            Spacer(
                modifier = Modifier
                    .width(width)
                    .height(indicatorHeight.dp)
                    .background(color, androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
        }
    }
}
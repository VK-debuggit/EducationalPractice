package com.example.educationalpractice.View

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.Data.Components.CustomButton
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.*

@Composable
fun OnboardScreen2(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.Accent),
                        colorResource(R.color.Disable),
                        colorResource(id = R.color.Disable).copy(alpha = 0.8f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(0.15f))

        // Изображение с анимацией прихода
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    animationSpec = tween(400),
                    initialOffsetX = { it }
                ) + fadeIn(animationSpec = tween(400)),
                exit = slideOutHorizontally(
                    animationSpec = tween(400),
                    targetOffsetX = { -it }
                ) + fadeOut(animationSpec = tween(400))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_2),
                    contentDescription = "Модная коллекция",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.mes_lets_go),
                style = Typography.displayLarge,
                color = Block,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.dec),
                style = Typography.titleLarge,
                color = SubTextLight,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.weight(0.05f))

        // Индикаторы (вторая страница активна)
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Неактивный индикатор
            Spacer(
                modifier = Modifier
                    .width(28.dp)
                    .height(4.dp)
                    .background(SubTextLight.copy(alpha = 0.3f), androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
            // Активный индикатор (вторая страница)
            Spacer(
                modifier = Modifier
                    .width(43.dp)
                    .height(4.dp)
                    .background(Block, androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
            // Неактивный индикатор
            Spacer(
                modifier = Modifier
                    .width(28.dp)
                    .height(4.dp)
                    .background(SubTextLight.copy(alpha = 0.3f), androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
        }

        Spacer(Modifier.weight(0.05f))

        // Кнопка "Далее"
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            CustomButton(
                onClick = {
                    // Переход на третий экран
                    NavigationManager.navigateTo(Views.OnboardScreen3.route)
                },
                text = stringResource(R.string.next),
                modifier = Modifier.fillMaxWidth(),
                containerColor = Block,
                contentColor = Text,
                cornerRadius = 13
            )
        }

        Spacer(Modifier.weight(0.1f))
    }
}

@Preview
@Composable
private fun OnboardScreen2Preview() {
    EducationalPracticeTheme {
        OnboardScreen2()
    }
}
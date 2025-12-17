package com.example.educationalpractice.Data.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.educationalpractice.ui.theme.Block
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.ui.theme.Splash
import com.example.educationalpractice.ui.theme.SubTextLight
import com.example.educationalpractice.ui.theme.Text
import com.example.educationalpractice.ui.theme.Typography
import com.example.educationalpractice.R
import androidx.compose.ui.res.stringResource

@Composable
fun OnboardScreen(
    // Параметры изображения
    imageResId: Int,
    imageDescription: String = "Onboarding image",

    // Параметры текста
    title: String,
    subtitle: String,
    buttonText: String,

    // Стили текста
    titleTextStyle: TextStyle = Typography.displayLarge,
    subtitleTextStyle: TextStyle = Typography.titleMedium,

    // Цвета
    titleColor: Color = Block,
    subtitleColor: Color = SubTextLight,
    backgroundColor: Color = Splash,
    buttonContainerColor: Color = Block,
    buttonContentColor: Color = Text,

    // Отступы и позиционирование
    imageAspectRatio: Float = 3f / 4f,
    titleTopSpacerWeight: Float = 0.05f,
    buttonBottomSpacerWeight: Float = 0.1f,
    middleSpacerWeight: Float = 0.3f,
    titleSubtitleSpacing: Dp = 8.dp,

    // Действие кнопки
    onButtonClick: () -> Unit,

    // Модификатор
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Изображение
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = imageDescription,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(imageAspectRatio)
        )

        // Отступ после изображения
        Spacer(modifier = Modifier.weight(titleTopSpacerWeight))

        // Заголовок
        Text(
            text = title,
            style = titleTextStyle,
            color = titleColor,
            modifier = Modifier.padding(horizontal = 20.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Отступ между заголовком и подзаголовком
        Spacer(modifier = Modifier.height(titleSubtitleSpacing))

        // Подзаголовок
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = subtitleTextStyle,
                color = subtitleColor,
                modifier = Modifier.padding(horizontal = 20.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        // Гибкое пространство между текстом и кнопкой
        Spacer(modifier = Modifier.weight(middleSpacerWeight))

        // Кнопка
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            CustomButton(
                onClick = onButtonClick,
                text = buttonText,
                modifier = Modifier.fillMaxWidth(),
                containerColor = buttonContainerColor,
                contentColor = buttonContentColor,
                cornerRadius = 13
            )
        }

        // Нижний отступ
        Spacer(modifier = Modifier.weight(buttonBottomSpacerWeight))
    }
}
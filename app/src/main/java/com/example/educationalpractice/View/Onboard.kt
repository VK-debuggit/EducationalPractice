package com.example.educationalpractice.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun Onboard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Splash),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхняя часть - больше отступа
        Spacer(Modifier.weight(0.3f))

        // Текст сверху
        Text(
            text = stringResource(R.string.welcome_mes),
            style = CustomTypography.headingBold30,
            color = Block,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // Изображение в середине
        Image(
            painter = painterResource(id = R.drawable.image_1),
            contentDescription = "Добро пожаловать",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        )

        Spacer(Modifier.weight(0.1f))

        // Индикаторы (первый активен)
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Активный индикатор (первая страница)
            Spacer(
                modifier = Modifier
                    .width(43.dp)
                    .height(4.dp)
                    .background(Block, androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
            // Неактивные индикаторы
            Spacer(
                modifier = Modifier
                    .width(28.dp)
                    .height(4.dp)
                    .background(SubTextLight.copy(alpha = 0.3f), androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
            Spacer(
                modifier = Modifier
                    .width(28.dp)
                    .height(4.dp)
                    .background(SubTextLight.copy(alpha = 0.3f), androidx.compose.foundation.shape.RoundedCornerShape(50))
            )
        }

        Spacer(Modifier.weight(0.1f))

        // Кнопка "Далее"
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            CustomButton(
                onClick = {
                    // Переход на второй экран
                    NavigationManager.navigateTo(Views.OnboardScreen2.route)
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
private fun OnboardPreview() {
    EducationalPracticeTheme {
        Onboard()
    }
}
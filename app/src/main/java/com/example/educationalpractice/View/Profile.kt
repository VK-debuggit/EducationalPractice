// ProfileFormScreen.kt (исправленная версия)
package com.yourpackage.ui.screens

import android.R.attr.name
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educationalpractice.R
import com.example.educationalpractice.ui.theme.Accent
import com.example.educationalpractice.ui.theme.Background
import com.example.educationalpractice.ui.theme.EducationalPracticeTheme
import com.example.educationalpractice.ui.theme.Text
import com.example.educationalpractice.ui.theme.Typography
import com.yourpackage.ui.components.BottomNavigationComponent

@Composable
fun ProfileFormScreen(
//    homeIcon: Int,
//    favoriteIcon: Int,
//    bagIcon: Int,
//    ordersIcon: Int,
//    profileIcon: Int,
//    onNavigationItemSelected: (Int) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var numberPhone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Верхняя часть с заголовком
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.Profile),
                style = Typography.labelLarge,
                color = Text
            )

            Box(
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        color = Accent,
                        shape = CircleShape
                    )
                    .clickable {
                        // Обработчик нажатия на иконку настроек
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Изменить",
                    modifier = Modifier.size(9.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.photoprofile),
            contentDescription = "Фото профиля",
            modifier = Modifier
                .fillMaxWidth()
                .size(96.dp)
        )

        Text(
            text = stringResource(R.string.Profile),
            style = Typography.labelLarge,
            color = Text
        )

        Image(
            painter = painterResource(id = R.drawable.frameprofile),
            contentDescription = "Код",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
        )

        // Основной контент с полями формы
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Карточка с профилем (СТАТИЧНАЯ информация)
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Статичное поле "Имя"
                Text(
                    text = stringResource(R.string.YourName),
                    style = Typography.labelLarge
                )

                OutlinedTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Background)
                        .fillMaxWidth(),
                    value = name,
                    onValueChange = { name },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Background,
                        unfocusedContainerColor = Background,
                        disabledContainerColor = Background,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text(text = "Emmanuel") },
                    singleLine = true
                )

                // Статичное поле "Фамилия"
                Text(
                    text = stringResource(R.string.LastName),
                    style = Typography.labelLarge
                )

                OutlinedTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Background)
                        .fillMaxWidth(),
                    value = lastname,
                    onValueChange = { lastname = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Background,
                        unfocusedContainerColor = Background,
                        disabledContainerColor = Background,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text("Oyiboke") },
                    singleLine = true
                )

                // Статичное поле "Адрес"
                Text(
                    text = stringResource(R.string.Address),
                    style = Typography.labelLarge
                )

                OutlinedTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Background)
                        .fillMaxWidth(),
                    value = address,
                    onValueChange = { address = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Background,
                        unfocusedContainerColor = Background,
                        disabledContainerColor = Background,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text("Nigeria") },
                    singleLine = true
                )

                // Статичное поле "Телефон" (может быть пустым)
                Text(
                    text = stringResource(R.string.phone),
                    style = Typography.labelLarge
                )

                OutlinedTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Background)
                        .fillMaxWidth(),
                    value = numberPhone,
                    onValueChange = { numberPhone = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Background,
                        unfocusedContainerColor = Background,
                        disabledContainerColor = Background,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text("Nigeria") },
                    singleLine = true
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileFormScreenPreview() {
    EducationalPracticeTheme {
        ProfileFormScreen()
    }
}
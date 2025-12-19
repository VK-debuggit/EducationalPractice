// ProfileFormScreen.kt (обновленный с новым меню)
package com.yourpackage.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.educationalpractice.Data.Components.CustomButton
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.*
import com.yourpackage.ui.components.BottomNavigationComponent
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFormScreen() {
    val context = LocalContext.current
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    // Изначальные значения
    var name by remember { mutableStateOf("Emmanuel") }
    var lastname by remember { mutableStateOf("Oyiboke") }
    var address by remember { mutableStateOf("Nigeria") }
    var numberPhone by remember { mutableStateOf("") }

    // Сохраняем оригинальные значения для сравнения
    val originalName = remember { "Emmanuel" }
    val originalLastname = remember { "Oyiboke" }
    val originalAddress = remember { "Nigeria" }
    val originalNumberPhone = remember { "" }
    var originalImageUri by remember { mutableStateOf<Uri?>(null) }

    // Проверяем, были ли изменения
    val hasChanges = remember(name, lastname, address, numberPhone, capturedImageUri) {
        name != originalName ||
                lastname != originalLastname ||
                address != originalAddress ||
                numberPhone != originalNumberPhone ||
                capturedImageUri != originalImageUri
    }

    // Камера
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            capturedImageUri = currentPhotoUri
            Toast.makeText(context, "Фото обновлено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Фото не сделано", Toast.LENGTH_SHORT).show()
            capturedImageUri = null
            currentPhotoUri = null
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val photoFile = createImageFile(context)
            val photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile)
            currentPhotoUri = photoUri
            cameraLauncher.launch(photoUri)
        } else {
            Toast.makeText(context, "Нужно разрешение на камеру", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.Profile),
                            style = Typography.displayMedium,
                            color = Text
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    // Кнопка редактирования/сохранения
                    if (!isEditing) {
                        Box(
                            modifier = Modifier
                                .size(25.dp)
                                .background(
                                    color = Accent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    // Включаем режим редактирования
                                    isEditing = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.edit),
                                contentDescription = "Изменить",
                                modifier = Modifier.size(20.dp),
                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationComponent(
                homeIcon = R.drawable.home,
                favoriteIcon = R.drawable.favorite,
                bagIcon = R.drawable.bag_2,
                ordersIcon = R.drawable.orders,
                profileIcon = R.drawable.profile,
                initialSelectedItem = R.drawable.profile, // Начальный выбранный элемент - профиль
                onItemSelected = { selectedIcon ->
                    when (selectedIcon) {
                        R.drawable.home -> {
                            NavigationManager.navigateTo(Views.Home.route)
                        }
                        R.drawable.favorite -> {
                            // Переход в избранное
                        }
                        R.drawable.bag_2 -> {
                            // Переход в корзину
                        }
                        R.drawable.orders -> {
                            // Переход к заказам
                        }
                        R.drawable.profile -> {
                            // Уже на профиле
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Фото профиля
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clickable(enabled = isEditing) {
                                    if (isEditing) {
//                                        checkCameraPermission(context, permissionLauncher)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (capturedImageUri != null) {
                                val bitmapState = remember(capturedImageUri) {
                                    loadImageBitmap(context, capturedImageUri!!)
                                }
                                bitmapState?.let { bitmap ->
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = "Фото профиля",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                } ?: run {
                                    Image(
                                        painter = painterResource(id = R.drawable.photoprofile),
                                        contentDescription = "Ошибка загрузки фото",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                }
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.photoprofile),
                                    contentDescription = "Добавить фото",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }

                            if (isEditing) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.4f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Сменить фото",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Текст под фото (всегда видимый)
                        Text(
                            text = "Emmanuel Oyiboke",
                            style = Typography.headlineSmall,
                            color = Text,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        // Дополнительный текст (появляется при редактировании)
                        if (isEditing) {
                            Text(
                                text = stringResource(R.string.Change),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Accent,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }

                // Код профиля
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.frameprofile),
                        contentDescription = "Код",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                }

                // Форма с полями
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    // Поле "Имя"
                    Text(
                        text = stringResource(R.string.YourName),
                        style = Typography.labelMedium,
                        color = Text.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ProfileField(
                        value = name,
                        onValueChange = { if (isEditing) name = it },
                        isEditing = isEditing,
                        placeholder = "Emmanuel",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )

                    // Поле "Фамилия"
                    Text(
                        text = stringResource(R.string.LastName),
                        style = Typography.labelMedium,
                        color = Text.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ProfileField(
                        value = lastname,
                        onValueChange = { if (isEditing) lastname = it },
                        isEditing = isEditing,
                        placeholder = "Oyiboke",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )

                    // Поле "Адрес"
                    Text(
                        text = stringResource(R.string.Address),
                        style = Typography.labelMedium,
                        color = Text.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ProfileField(
                        value = address,
                        onValueChange = { if (isEditing) address = it },
                        isEditing = isEditing,
                        placeholder = "Nigeria",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )

                    // Поле "Телефон"
                    Text(
                        text = stringResource(R.string.phone),
                        style = Typography.labelMedium,
                        color = Text.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ProfileField(
                        value = numberPhone,
                        onValueChange = { if (isEditing) numberPhone = it },
                        isEditing = isEditing,
                        placeholder = "+7 811-732-5298",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (isEditing) {
                        CustomButton(
                            onClick = {
                                // Сохраняем изменения и выходим из режима редактирования
                                isEditing = false
                                Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()

                                // Обновляем оригинальные значения
                                originalImageUri = capturedImageUri
                                // Здесь можно добавить логику сохранения в базу данных или SharedPreferences
                            },
                            text = stringResource(R.string.save),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                            enabled = hasChanges, // Кнопка активна только если есть изменения
                            cornerRadius = 12,
                            textStyle = Typography.labelSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp)) // Отступ для меню
                }
            }
        }
    }
}

@Composable
fun ProfileField(
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        if (isEditing) {
            // В режиме редактирования - поле ввода
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Text
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Text.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                }
            )
        } else {
            // В режиме просмотра - просто текст
            Text(
                text = if (value.isNotEmpty()) value else placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = if (value.isNotEmpty()) Text else Text.copy(alpha = 0.5f)
            )
        }
    }
}

// Остальные функции остаются без изменений
fun loadImageBitmap(context: Context, uri: Uri): androidx.compose.ui.graphics.ImageBitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun checkCameraPermission(
    context: Context,
    permissionLauncher: ActivityResultLauncher<String>,
    cameraLauncher: ActivityResultLauncher<Uri>,
    photoUri: Uri
) {
    val hasPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    if (hasPermission) {
        cameraLauncher.launch(photoUri)
    } else {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

fun openCamera(
    context: Context,
    cameraLauncher: ActivityResultLauncher<Uri>,
    onUriCreated: (Uri) -> Unit
) {
    try {
        val photoFile = createImageFile(context)
        val photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
        onUriCreated(photoUri)
        cameraLauncher.launch(photoUri)
    } catch (e: Exception) {
        Toast.makeText(context, "Ошибка камеры: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "profile_${timeStamp}_",
        ".jpg",
        storageDir
    )
}

@Preview
@Composable
private fun ProfileFormScreenPreview() {
    EducationalPracticeTheme {
        ProfileFormScreen()
    }
}
package com.example.educationalpractice.ui.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.educationalpractice.Data.Components.CustomButton
import com.example.educationalpractice.R
import com.example.educationalpractice.navigation.NavigationManager
import com.example.educationalpractice.navigation.Views
import com.example.educationalpractice.ui.theme.*
import com.example.educationalpractice.ui.theme.ViewModel.ProfileViewModel
import com.yourpackage.ui.components.BottomNavigationComponent
import kotlinx.coroutines.delay
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFormScreen() {
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel()
    var isEditing by remember { mutableStateOf(false) }
    var showSaveSuccess by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoFile by remember { mutableStateOf<File?>(null) }

    // Загружаем профиль при первом открытии экрана
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(context)
    }

    // Launcher для камеры
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoFile != null) {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                currentPhotoFile!!
            )
            capturedImageUri = uri
            profileViewModel.photoUri = uri
            Toast.makeText(context, "Фото обновлено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Фото не сделано", Toast.LENGTH_SHORT).show()
        }
        currentPhotoFile = null
    }

    // Launcher для разрешения камеры
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            currentPhotoFile = createImageFile(context)
            currentPhotoFile?.let { file ->
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                cameraLauncher.launch(uri)
            } ?: run {
                Toast.makeText(context, "Ошибка создания файла", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Нужно разрешение на камеру", Toast.LENGTH_SHORT).show()
        }
    }

    // Функция для открытия камеры
    fun openCamera() {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )

        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            currentPhotoFile = createImageFile(context)
            currentPhotoFile?.let { file ->
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                cameraLauncher.launch(uri)
            } ?: run {
                Toast.makeText(context, "Ошибка создания файла", Toast.LENGTH_SHORT).show()
            }
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Обработка успешного сохранения
    LaunchedEffect(showSaveSuccess) {
        if (showSaveSuccess) {
            delay(2000)
            showSaveSuccess = false
            isEditing = false
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
                    if (!isEditing) {
                        Box(
                            modifier = Modifier
                                .size(25.dp)
                                .background(
                                    color = Accent,
                                    shape = CircleShape
                                )
                                .clickable {
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
                initialSelectedItem = R.drawable.profile,
                onItemSelected = { selectedIcon ->
                    when (selectedIcon) {
                        R.drawable.home -> {
                            NavigationManager.navigateTo(Views.Home.route)
                        }
                        R.drawable.favorite -> {}
                        R.drawable.bag_2 -> {}
                        R.drawable.orders -> {}
                        R.drawable.profile -> {}
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
                                .size(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Показываем фото или дефолтное изображение
                            val imageToShow = capturedImageUri ?: profileViewModel.photoUri

                            if (imageToShow != null) {
                                val bitmap = loadImageBitmap(context, imageToShow)
                                bitmap?.let {
                                    Image(
                                        bitmap = it,
                                        contentDescription = "Фото профиля",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                } ?: run {
                                    DefaultProfileImage()
                                }
                            } else {
                                DefaultProfileImage()
                            }
                        }

                        // Имя из таблицы профилей
                        Text(
                            text = profileViewModel.getFullName(),
                            style = Typography.headlineSmall,
                            color = Text,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        // Текст для смены фото (кликабельный только в режиме редактирования)
                        if (isEditing) {
                            Text(
                                text = stringResource(R.string.Change),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Accent,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .clickable {
                                        openCamera()
                                    }
                            )
                        }
                    }
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
                        value = profileViewModel.name,
                        onValueChange = { if (isEditing) profileViewModel.name = it },
                        isEditing = isEditing,
                        placeholder = "Введите имя",
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
                        value = profileViewModel.lastName,
                        onValueChange = { if (isEditing) profileViewModel.lastName = it },
                        isEditing = isEditing,
                        placeholder = "Введите фамилию",
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
                        value = profileViewModel.address,
                        onValueChange = { if (isEditing) profileViewModel.address = it },
                        isEditing = isEditing,
                        placeholder = "Введите адрес",
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
                        value = profileViewModel.phone,
                        onValueChange = { if (isEditing) profileViewModel.phone = it },
                        isEditing = isEditing,
                        placeholder = "+7 000-000-0000",
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Показать ошибку если есть
                    if (profileViewModel.errorMessage.isNotEmpty()) {
                        Text(
                            text = profileViewModel.errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (isEditing) {
                        CustomButton(
                            onClick = {
                                val saved = profileViewModel.saveProfile(context)
                                if (saved) {
                                    showSaveSuccess = true
                                    Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                                }
                            },
                            text = stringResource(R.string.save),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                            enabled = !profileViewModel.isLoading,
                            cornerRadius = 12,
                            textStyle = Typography.labelSmall
                        )
                    }

                    // Показать успех сохранения
                    if (showSaveSuccess) {
                        Text(
                            text = "Данные сохранены!",
                            color = Color.Green,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            // Показать индикатор загрузки
            if (profileViewModel.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Accent)
                }
            }
        }
    }
}

@Composable
private fun DefaultProfileImage() {
    Image(
        painter = painterResource(id = R.drawable.photoprofile),
        contentDescription = "Фото профиля",
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
    )
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
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Text),
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
            Text(
                text = if (value.isNotEmpty()) value else placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = if (value.isNotEmpty()) Text else Text.copy(alpha = 0.5f)
            )
        }
    }
}

fun loadImageBitmap(context: Context, uri: Uri): androidx.compose.ui.graphics.ImageBitmap? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
        }
    } catch (e: Exception) {
        null
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
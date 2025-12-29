package ru.itis.android.navScreen.creator

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import kotlinx.coroutines.launch
import ru.itis.android.R
import ru.itis.android.utils.ResManager
import ru.itis.android.data.users.UserSession
import ru.itis.android.di.ServiceLocator
import ru.itis.android.model.GameDataModel
import ru.itis.android.utils.ImageStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(
    onGameCreated: () -> Unit = {},
    resManager: ResManager
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val gameRepository = remember { ServiceLocator.getGameRepository() }

    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    var imageUriString by remember { mutableStateOf<String?>(null) }

    var isGenreExpanded by remember { mutableStateOf(false) }
    val genres = listOf(
        "Экшен", "РПГ", "Стратегия", "Гонки",
        "Спорт", "Хоррор", "Инди", "Пазл", "Аркада", "Приключение"
    )

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { selectedUri ->
            scope.launch {
                try {
                    val savedUri = ImageStorage.saveImageToInternalStorage(context, selectedUri)
                    imageUriString = savedUri

                    val fileSize = ImageStorage.getImageFileSize(context, savedUri)
                    snackbarHostState.showSnackbar(resManager.getString(R.string.image_saved, fileSize / 1024))

                    val inputStream = context.contentResolver.openInputStream(selectedUri)
                    val bytes = inputStream?.readBytes()
                    bytes?.let {
                        val bitmap = android.graphics.BitmapFactory.decodeByteArray(it, 0, it.size)
                        imageUri = bitmap.asImageBitmap()
                    }
                    inputStream?.close()

                } catch (e: Exception) {
                    snackbarHostState.showSnackbar(resManager.getString(R.string.error_saving_image, e.message ?: ""))
                }
            }
        }
    }

    val scrollState = rememberScrollState()
    val currentUsername = UserSession.getCurrentUsername()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.creator_title)) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        // Основной контент экрана
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Блок для выбора изображения
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        // Открываем галерею для выбора изображения
                        pickImageLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUriString != null) {
                        // Показываем выбранное изображение через Coil
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(imageUriString)
                                .build(),
                            contentDescription = stringResource(R.string.add_image_hint),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Показываем плейсхолдер, если изображение не выбрано
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_add_a_photo_24),
                                contentDescription = stringResource(R.string.add_image_description),
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.add_image_hint),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.game_title_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = title.isBlank()
            )

            // Выпадающий список для выбора жанра
            ExposedDropdownMenuBox(
                expanded = isGenreExpanded,
                onExpandedChange = { isGenreExpanded = !isGenreExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text(stringResource(R.string.genre_hint)) },
                    readOnly = true, // Поле только для чтения
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenreExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    isError = genre.isBlank()
                )

                // Выпадающее меню с жанрами
                ExposedDropdownMenu(
                    expanded = isGenreExpanded,
                    onDismissRequest = { isGenreExpanded = false }
                ) {
                    genres.forEach { genreItem ->
                        DropdownMenuItem(
                            text = { Text(genreItem) },
                            onClick = {
                                genre = genreItem
                                isGenreExpanded = false
                            }
                        )
                    }
                }
            }

            // Поле для ввода рейтинга
            OutlinedTextField(
                value = rating,
                onValueChange = {
                    // Разрешаем только цифры и максимум 3 символа (0-100)
                    if (it.all { char -> char.isDigit() } && it.length <= 3) {
                        rating = it
                    }
                },
                label = { Text(stringResource(R.string.rating_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = rating.isBlank() || rating.toIntOrNull()?.let { it !in 0..100 } ?: true
            )

            // Поле для ввода описания
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            // Кнопка создания игры
            Button(
                onClick = {
                    // Проверяем валидность данных
                    if (validateInput(title, genre, rating)) {
                        scope.launch {
                            try {
                                val currentUserId = UserSession.getCurrentUserId()
                                if (currentUserId == null) {
                                    snackbarHostState.showSnackbar(resManager.getString(R.string.error_unauthorized_creator))
                                    return@launch
                                }

                                // Создаем модель игры
                                val gameModel = GameDataModel(
                                    title = title,
                                    genre = genre,
                                    rating = rating.toInt(),
                                    date = getCurrentDate(),
                                    description = description,
                                    authorId = currentUserId,
                                    imageUri = imageUriString ?: "" // Сохраняем URI как строку
                                )

                                // Сохраняем игру в базу данных
                                gameRepository.createGame(gameModel)
                                snackbarHostState.showSnackbar(resManager.getString(R.string.game_created))

                                // Очищаем форму
                                title = ""
                                genre = ""
                                rating = ""
                                description = ""
                                imageUri = null
                                imageUriString = null

                                // Вызываем колбэк
                                onGameCreated()

                            } catch (e: Exception) {
                                // Показываем ошибку
                                snackbarHostState.showSnackbar(resManager.getString(R.string.error_creating_game, e.message ?: ""))
                            }
                        }
                    } else {
                        // Показываем сообщение об ошибке валидации
                        scope.launch {
                            snackbarHostState.showSnackbar(resManager.getString(R.string.validation_error))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = validateInput(title, genre, rating) // Кнопка активна только при валидных данных
            ) {
                Text(stringResource(R.string.create_game_button))
            }

            // Блок с информацией об авторе
            if (currentUsername != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUsername.first().toString(),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.author_label),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = currentUsername,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (UserSession.getCurrentUserId() == null) {
            snackbarHostState.showSnackbar(resManager.getString(R.string.login_required))
        }
    }
}

private fun validateInput(title: String, genre: String, rating: String): Boolean {
    if (title.isBlank() || genre.isBlank() || rating.isBlank()) {
        return false
    }

    val ratingInt = rating.toIntOrNull()
    return ratingInt != null && ratingInt in 0..100
}

private fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}
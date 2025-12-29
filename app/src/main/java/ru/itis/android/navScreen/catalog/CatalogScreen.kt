package ru.itis.android.navScreen.userGames

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.R
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import kotlinx.coroutines.launch
import ru.itis.android.data.users.UserSession
import ru.itis.android.di.ServiceLocator
import ru.itis.android.model.GameDataModel
import ru.itis.android.utils.ImageStorage
import ru.itis.android.utils.ResManager

enum class SortType {
    DATE_NEWEST,
    DATE_OLDEST,
    RATING_HIGHEST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(resManager: ResManager) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Состояние для Bottom Sheet
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var userGames by remember { mutableStateOf<List<GameDataModel>>(emptyList()) }
    var currentSortType by remember { mutableStateOf(SortType.DATE_NEWEST) }

    val gameRepository = remember { ServiceLocator.getGameRepository() }

    // Функция загрузки игр
    fun loadUserGames() {
        scope.launch {
            try {
                isLoading = true
                val currentUserId = UserSession.getCurrentUserId()
                if (currentUserId == null) {
                    snackbarHostState.showSnackbar(resManager.getString(R.string.error_unauthorized))
                    return@launch
                }
                userGames = gameRepository.getGamesByAuthor(currentUserId)
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(resManager.getString(R.string.error_loading_games, e.message ?: ""))
            } finally {
                isLoading = false
            }
        }
    }

    // Функция сортировки игр
    val sortedGames = remember(userGames, currentSortType) {
        when (currentSortType) {
            SortType.DATE_NEWEST -> userGames.sortedByDescending { it.createdAt }
            SortType.DATE_OLDEST -> userGames.sortedBy { it.createdAt }
            SortType.RATING_HIGHEST -> userGames.sortedByDescending { it.rating }
        }
    }

    // Получаем иконку для текущего типа сортировки
    val currentSortIcon = when (currentSortType) {
        SortType.DATE_NEWEST -> Icons.Default.KeyboardArrowDown
        SortType.DATE_OLDEST -> Icons.Default.KeyboardArrowUp
        SortType.RATING_HIGHEST -> Icons.Default.Star
    }

    // Получаем название текущего типа сортировки
    val currentSortText = when (currentSortType) {
        SortType.DATE_NEWEST -> stringResource(R.string.sort_option_newest)
        SortType.DATE_OLDEST -> stringResource(R.string.sort_option_oldest)
        SortType.RATING_HIGHEST -> stringResource(R.string.sort_option_rating)
    }

    // Загружаем при первом запуске
    LaunchedEffect(Unit) {
        loadUserGames()
    }

    // Bottom Sheet для сортировки
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            tonalElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            SortOptionsBottomSheet(
                currentSortType = currentSortType,
                onSortSelected = { sortType ->
                    currentSortType = sortType
                    showBottomSheet = false
                }
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.catalog_title, sortedGames.size),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    // Кнопка сортировки
                    IconButton(
                        onClick = { showBottomSheet = true },
                        enabled = userGames.isNotEmpty() && !isLoading
                    ) {
                        Icon(
                            imageVector = currentSortIcon,
                            contentDescription = stringResource(R.string.sort_button_description)
                        )
                    }

                    // Кнопка обновления
                    IconButton(
                        onClick = { loadUserGames() },
                        enabled = !isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh_button_description)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading && userGames.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (userGames.isEmpty()) {
                EmptyGamesMessage(
                    onRefresh = { loadUserGames() }
                )
            } else {
                // Показываем индикатор поверх списка при обновлении
                Box(modifier = Modifier.fillMaxSize()) {
                    UserGamesList(
                        games = sortedGames,
                        modifier = Modifier.fillMaxSize()
                    )

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(16.dp)
                        )
                    }
                }
            }

            // Индикатор текущей сортировки (BottomSheet)
            if (userGames.isNotEmpty()) {
                CurrentSortIndicator(
                    sortText = currentSortText,
                    onClick = { showBottomSheet = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SortOptionsBottomSheet(
    currentSortType: SortType,
    onSortSelected: (SortType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Заголовок
        Text(
            text = stringResource(R.string.sort_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        // Варианты сортировки
        SortOptionItem(
            icon = Icons.Default.KeyboardArrowDown,
            text = stringResource(R.string.sort_option_newest),
            isSelected = currentSortType == SortType.DATE_NEWEST,
            onClick = { onSortSelected(SortType.DATE_NEWEST) }
        )

        SortOptionItem(
            icon = Icons.Default.KeyboardArrowUp,
            text = stringResource(R.string.sort_option_oldest),
            isSelected = currentSortType == SortType.DATE_OLDEST,
            onClick = { onSortSelected(SortType.DATE_OLDEST) }
        )

        SortOptionItem(
            icon = Icons.Default.Star,
            text = stringResource(R.string.sort_option_rating),
            isSelected = currentSortType == SortType.RATING_HIGHEST,
            onClick = { onSortSelected(SortType.RATING_HIGHEST) }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SortOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.selected_indicator),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun CurrentSortIndicator(
    sortText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.current_sort_description),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = sortText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EmptyGamesMessage(
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_games_message),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = onRefresh,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.refresh_button))
        }
    }
}

@Composable
private fun UserGamesList(
    games: List<GameDataModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Для каждой игры в списке создаем карточку
        items(games) { game ->
            GameCard(game = game)
        }
    }
}

@Composable
private fun GameCard(game: GameDataModel) {
    val context = LocalContext.current
    var imageAvailable by remember { mutableStateOf(true) }

    // Проверяем доступность изображения
    LaunchedEffect(game.imageUri) {
        if (game.imageUri.isNotBlank()) {
            imageAvailable = ImageStorage.isImageAvailable(context, game.imageUri)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = game.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.genre_label, game.genre),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = stringResource(R.string.rating_label, game.rating),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = stringResource(R.string.created_label, game.date),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Изображение игры (если есть URI)
            if (game.imageUri.isNotBlank() && imageAvailable) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(ImageStorage.getDisplayUri(game.imageUri))
                        .crossfade(true)
                        .placeholder(R.drawable.ic_baseline_image_24)
                        .error(R.drawable.ic_baseline_broken_image_24)
                        .build(),
                    contentDescription = stringResource(R.string.game_image_description, game.title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Показываем плейсхолдер если изображение недоступно
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_android_24),
                        contentDescription = stringResource(R.string.image_unavailable),
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (game.description.isNotBlank()) {
                Text(
                    text = game.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 12.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
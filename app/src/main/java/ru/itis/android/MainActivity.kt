package ru.itis.android

import ProfileScreen
import android.Manifest
import android.content.Intent
import ru.itis.android.navScreen.signin.SignInScreen
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.itis.android.di.ServiceLocator
import ru.itis.android.model.BottomNavTabs
import ru.itis.android.model.SampleReceiver
import ru.itis.android.navScreen.catalog.CatalogScreen
import ru.itis.android.navScreen.creator.CreatorScreen
import ru.itis.android.navScreen.signup.SignUpScreen
import ru.itis.android.navigation.ProfileObject
import ru.itis.android.navigation.CatalogObject
import ru.itis.android.navigation.CreatorObject
import ru.itis.android.utils.PermissionHandler
import ru.itis.android.utils.ResManager

class MainActivity : ComponentActivity() {

    private var permissionsHandler: PermissionHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionsHandler = PermissionHandler(
            onPermissionGranted = {},
            onPermissionDenied = {},
            activity = this
        )
        val resManager = ResManager(ctx = applicationContext)

        val receiver = SampleReceiver()
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsHandler?.requestMultiplePermission(
                    permission = listOf(Manifest.permission.POST_NOTIFICATIONS)
                )
            }
        }

        setContent {
            // Основной навигационный контроллер
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Определяем, показывать ли Bottom Navigation
            val showBottomNav = when (currentRoute) {
                CatalogObject.route, CreatorObject.route, ProfileObject.route -> true
                else -> false
            }

            // Выбранная вкладка
            var selectedTab by rememberSaveable { mutableIntStateOf(0) }

            // Обновляем выбранную вкладку при изменении маршрута
            LaunchedEffect(currentRoute) {
                selectedTab = when (currentRoute) {
                    CatalogObject.route -> 0
                    CreatorObject.route -> 1
                    ProfileObject.route -> 2
                    else -> 0
                }
            }

            Scaffold(
                bottomBar = {
                    if (showBottomNav) {
                        NavigationBar(
                            windowInsets = NavigationBarDefaults.windowInsets
                        ) {
                            getBottomNavTabs(resManager).forEachIndexed { index, destination ->
                                NavigationBarItem(
                                    selected = selectedTab == index,
                                    onClick = {
                                        selectedTab = index
                                        navController.navigate(destination.route.toString()) {
                                            // Очищаем стек при переключении вкладок
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            destination.icon,
                                            contentDescription = destination.contentDescription
                                        )
                                    },
                                    label = {
                                        Text(text = destination.label)
                                    },
                                )
                            }
                        }
                    }
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = "signIn", // Начинаем с экрана входа
                    modifier = Modifier.padding(paddingValues)
                ) {
                    // Экран входа
                    composable("signIn") {
                        SignInScreen(navController = navController)
                    }

                    // Экран регистрации
                    composable("signUp") {
                        SignUpScreen(navController = navController)
                    }

                    // Экран каталога (список контента)
                    composable<CatalogObject> {
                        CatalogScreen()
                    }

                    // Экран создания (добавление контента)
                    composable<CreatorObject> {
                        CreatorScreen()
                    }

                    // Экран профиля
                    composable<ProfileObject> {
                        ProfileScreen(
                            navController = navController,
                            userRepository = ServiceLocator.getUserRepository()
                        )
                    }
                }
            }
        }
    }

    private fun getBottomNavTabs(resManager: ResManager): List<BottomNavTabs> = listOf(
        BottomNavTabs(
            route = CatalogObject,
            label = "Каталог",
            icon = Icons.Default.List,
            contentDescription = "Каталог фильмов"
        ),
        BottomNavTabs(
            route = CreatorObject,
            label = "Добавить",
            icon = Icons.Default.Add,
            contentDescription = "Добавить фильм"
        ),
        BottomNavTabs(
            route = ProfileObject,
            label = "Профиль",
            icon = Icons.Default.Person,
            contentDescription = "Профиль пользователя"
        )
    )
}
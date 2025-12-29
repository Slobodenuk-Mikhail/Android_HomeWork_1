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
import ru.itis.android.data.users.UserSession
import ru.itis.android.di.ServiceLocator
import ru.itis.android.model.BottomNavTabs
import ru.itis.android.model.SampleReceiver
import ru.itis.android.navScreen.creator.CreatorScreen
import ru.itis.android.navScreen.signup.SignUpScreen
import ru.itis.android.navScreen.userGames.CatalogScreen
import ru.itis.android.navigation.ProfileObject
import ru.itis.android.navigation.CatalogObject
import ru.itis.android.navigation.CreatorObject
import ru.itis.android.navigation.SignInObject
import ru.itis.android.navigation.SignUpObject
import ru.itis.android.utils.PermissionHandler
import ru.itis.android.utils.ResManager

class MainActivity : ComponentActivity() {

    private lateinit var resManager: ResManager
    private var permissionsHandler: PermissionHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ServiceLocator.initDatabase(applicationContext)
        UserSession.initialize(this)
        resManager = ResManager(applicationContext)

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
            val startDestination = if (UserSession.isLogged()) {
                CatalogObject.route
            } else {
                SignInObject.route
            }

            // Основной навигационный контроллер
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Определяем, показывать ли Bottom Navigation
            val showBottomNav = when (currentRoute) {
                CatalogObject.route,
                CreatorObject.route,
                ProfileObject.route -> true
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
                                        navController.navigate(destination.route) {
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
                    startDestination = startDestination,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable(SignInObject.route) {
                        SignInScreen(
                            navController = navController,
                            resManager = resManager
                        )
                    }

                    composable(SignUpObject.route) {
                        SignUpScreen(
                            navController = navController,
                            resManager = resManager
                        )
                    }

                    composable(CatalogObject.route) {
                        CatalogScreen(resManager = resManager)
                    }

                    composable(CreatorObject.route) {
                        CreatorScreen(resManager = resManager)
                    }

                    composable(ProfileObject.route) {
                        ProfileScreen(
                            navController = navController,
                            userRepository = ServiceLocator.getUserRepository(),
                            resManager = resManager
                        )
                    }
                }
            }
        }
    }

    private fun getBottomNavTabs(resManager: ResManager): List<BottomNavTabs> = listOf(
        BottomNavTabs(
            route = CatalogObject.route,
            label = resManager.getString(R.string.catalog_tab),
            icon = Icons.Default.List,
            contentDescription = resManager.getString(R.string.catalog_tab_description)
        ),
        BottomNavTabs(
            route = CreatorObject.route,
            label = resManager.getString(R.string.creator_tab),
            icon = Icons.Default.Add,
            contentDescription = resManager.getString(R.string.creator_tab_description)
        ),
        BottomNavTabs(
            route = ProfileObject.route,
            label = resManager.getString(R.string.profile_tab),
            icon = Icons.Default.Person,
            contentDescription = resManager.getString(R.string.profile_tab_description)
        )
    )
}
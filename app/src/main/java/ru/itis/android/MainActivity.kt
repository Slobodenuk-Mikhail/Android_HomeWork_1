package ru.itis.android

import MainPageScreen
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.android.navScreen.viewTasks.ViewTasksScreen
import ru.itis.android.navigation.NavigationIds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = NavigationIds.MAIN_PAGE.name
            ) {
                composable (route = NavigationIds.MAIN_PAGE.name) {
                    MainPageScreen(navController = navController)
                }
                composable(route = NavigationIds.VIEW_TASKS.name){
                    ViewTasksScreen()
                }
            }
        }
    }

    private companion object {
        const val LOG_TAG = "MainActivity"
    }
}
package ru.itis.android

import MainPageScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.itis.android.model.TaskDataModel
import ru.itis.android.navScreen.taskViewer.ViewTasksScreen
import ru.itis.android.navigation.CustomNavType
import ru.itis.android.navigation.MainPageStartObject
import ru.itis.android.navigation.ViewTasksObject
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = MainPageStartObject
            ) {
                composable< MainPageStartObject>{
                    MainPageScreen(navController = navController)
                }
                composable<ViewTasksObject>(
                    typeMap = mapOf(
                        typeOf<TaskDataModel>() to CustomNavType.TaskDataNavType
                    )
                ){ entry ->
                    val args = entry.toRoute<ViewTasksObject>()
                    ViewTasksScreen(
                        firstArg = args.taskTitle,
                        secondArg = args.taskText,
                        navController = navController,
                    )
                }
            }
        }
    }

    private companion object {
        const val LOG_TAG = "MainActivity"
    }
}
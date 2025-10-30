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
import ru.itis.android.navScreen.taskCreator.TaskCreatorScreen
import ru.itis.android.navScreen.taskViewer.TaskViewerScreen
import ru.itis.android.navigation.CustomNavType
import ru.itis.android.navigation.MainPageObject
import ru.itis.android.navigation.TaskCreatorObject
import ru.itis.android.navigation.TaskViewerObject
import kotlin.reflect.typeOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = MainPageObject
            ) {
                composable<MainPageObject>{
                    MainPageScreen(navController = navController)
                }
                composable<TaskViewerObject>(
                    typeMap = mapOf(
                        typeOf<ArrayList<TaskDataModel>>() to CustomNavType.ArrayListOfTasksDataNavType
                    )
                ){ entry ->
                    val args = entry.toRoute<TaskViewerObject>()
                    TaskViewerScreen(
                        userEmail = args.userEmail,
                        navController = navController
                    )
                }
                composable<TaskCreatorObject>(
                    typeMap = mapOf(
                        typeOf<ArrayList<TaskDataModel>>() to CustomNavType.ArrayListOfTasksDataNavType
                    )
                ) { entry ->
                    val args = entry.toRoute<TaskCreatorObject>()
                    TaskCreatorScreen(
                        arrayListOfTasks = args.arrayListOfTasks,
                        navController = navController
                    )
                }
            }
        }
    }

}
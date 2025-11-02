package ru.itis.android

import MainPageScreen
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.itis.android.navScreen.taskCreator.TaskCreatorScreen
import ru.itis.android.navScreen.taskViewer.TaskViewerScreen
import ru.itis.android.navigation.MainPageObject
import ru.itis.android.navigation.TaskCreatorObject
import ru.itis.android.navigation.TaskViewerObject
import ru.itis.android.utils.NotificationHandler
import ru.itis.android.utils.PermissionHandler

class MainActivity : ComponentActivity() {

    private var permissionsHandler: PermissionHandler?= null
    private var notificationHandler: NotificationHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (permissionsHandler == null) {
            permissionsHandler = PermissionHandler(
                onPermissionGranted = {}, onPermissionDenied = {}, activity = this
            )
        }


        if (notificationHandler == null) {
            notificationHandler = NotificationHandler(ctx = applicationContext)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
                ){
                permissionsHandler?.requestMultiplePermission(
                    permission = listOf(Manifest.permission.POST_NOTIFICATIONS)
                )
            } else {

            }

        }
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = MainPageObject
            ) {
                composable<MainPageObject>{
                    MainPageScreen(
                        navController = navController,
                        onButtonClick = { dataModel ->
                            notificationHandler?.showNotification(dataModel)

                        }
                    )
                }
                composable<TaskViewerObject> { entry ->
                    val args = entry.toRoute<TaskViewerObject>()
                    TaskViewerScreen(
                        userEmail = args.userEmail,
                        navController = navController)
                }
                composable<TaskCreatorObject>{ entry ->
                    TaskCreatorScreen(navController = navController)
                }
            }
        }
    }

}
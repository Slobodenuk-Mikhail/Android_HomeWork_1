package ru.itis.android

import MainPageScreen
import SignInScreen
import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.android.di.ServiceLocator
import ru.itis.android.model.BottomNavTabs
import ru.itis.android.model.SampleReceiver
import ru.itis.android.navScreen.taskCreator.UsersMessagesScreen
import ru.itis.android.navScreen.catalog.NotifEditorScreen
import ru.itis.android.navScreen.signUp.SignUpScreen
import ru.itis.android.navigation.NotifEditorObject
import ru.itis.android.navigation.NotifSettingsObject
import ru.itis.android.navigation.UsersMessagesObject
import ru.itis.android.utils.PermissionHandler
import ru.itis.android.utils.ResManager

class MainActivity : ComponentActivity() {


    private var permissionsHandler: PermissionHandler?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionsHandler =
            PermissionHandler(onPermissionGranted = {}, onPermissionDenied = {}, activity = this)
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

            val navController = rememberNavController()
//            val selectedTab = rememberSaveable { mutableIntStateOf(value = 0) }
//
//
//            Scaffold(
//                bottomBar = {
//                    NavigationBar(
//                        windowInsets = NavigationBarDefaults.windowInsets
//                    ) {
//                        getBottomNavTabs(resManager).forEachIndexed { index, destination ->
//                            NavigationBarItem(
//                                selected = selectedTab.intValue == index,
//                                onClick = {
//                                    selectedTab.intValue = index
//                                    navController.navigate(destination.route)
//                                },
//                                icon = {
//                                    Image(
//                                        imageVector = destination.icon,
//                                        contentDescription = destination.contentDescription
//                                    )
//                                },
//                                        label = {
//                                            Text(text = destination.label)
//                                        },
//                            )
//                        }
//                    }
//                }
//            ) { paddings ->
//                NavHost(
//                    navController = navController,
//                    startDestination = NotifSettingsObject
//                ) {
//
//                    composable<NotifSettingsObject> {
//                        MainPageScreen(
//                            navController = navController,
//                            userRepository = ServiceLocator.getUserRepository()
//                        )
//                    }
//
//                    composable<NotifEditorObject> {
//                        NotifEditorScreen()
//                    }
//
//                    composable<UsersMessagesObject> {
//                        UsersMessagesScreen()
//                    }
//
//                }
//
//            }

            NavHost(
                navController = navController,
                startDestination = "signIn"
            ) {
                composable("signIn") {
                    SignInScreen(navController = navController)
                }

                composable("signUp") {
                    SignUpScreen(navController = navController)
                }

                composable("main") {
                    MainPageScreen(
                        navController = navController,
                        userRepository = ServiceLocator.getUserRepository()
                    )
                }
            }
        }
    }
    
    private fun getBottomNavTabs(resManager: ResManager): List<BottomNavTabs> = listOf(

        BottomNavTabs(
            route = NotifSettingsObject,
            label = resManager.getString(R.string.notif_settings_label),
            icon = Icons.Default.Settings,
        ),
        BottomNavTabs(
            route = NotifEditorObject,
            label = resManager.getString(R.string.notif_editor_label),
            icon = Icons.Default.Edit
        ),
        BottomNavTabs(
            route = UsersMessagesObject,
            label = resManager.getString(R.string.users_messages_label),
            icon = Icons.Default.MailOutline
        )
    )


}
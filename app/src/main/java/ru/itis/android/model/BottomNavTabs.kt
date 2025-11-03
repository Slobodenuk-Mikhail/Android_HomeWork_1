package ru.itis.android.model


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import ru.itis.android.R
import ru.itis.android.navScreen.taskViewer.TaskViewerScreen
import ru.itis.android.navigation.MainPageObject
import ru.itis.android.navigation.TaskCreatorObject

enum class BottomNavTabs(
    val route: Any,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String? = null
) {
    //hard code, need to redone
    Main(route = MainPageObject,
        label = "Main",
        icon = Icons.Default.Settings,
    ),
    Profile (
        route = TaskCreatorObject,
        label = "Tasks",
        icon = Icons.Default.Edit
    )

}
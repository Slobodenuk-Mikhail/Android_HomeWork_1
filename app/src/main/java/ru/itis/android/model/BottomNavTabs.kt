package ru.itis.android.model


import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import ru.itis.android.R
import ru.itis.android.navigation.NotifEditorObject
import ru.itis.android.navigation.NotifSettingsObject
import ru.itis.android.navigation.UsersMessagesObject

enum class BottomNavTabs(
    val route: Any,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String? = null
) {
    //hard code, need to redo
    NotificationsSettings(
        route = NotifSettingsObject,
        label = "Settings",
        icon = Icons.Default.Settings,
    ),

    NotificationsEditor (
        route = NotifEditorObject,
        label = "Editor",
        icon = Icons.Default.Edit
    ),

    UsersMessenger (
        route = UsersMessagesObject,
        label = "Messengers",
        icon = Icons.Default.MailOutline
    )
}
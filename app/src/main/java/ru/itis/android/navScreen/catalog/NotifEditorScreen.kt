package ru.itis.android.navScreen.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import ru.itis.android.Keys
import ru.itis.android.R
import ru.itis.android.utils.ResManager

@Composable
fun NotifEditorScreen() {

    var notifId by remember { mutableStateOf("") }
    var notifNewText by remember { mutableStateOf("") }
    val ctx = LocalContext.current

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = notifId,
                onValueChange = { input ->
                    notifId = input
                },
                label = { Text(stringResource(R.string.id_label)) },
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = notifNewText,
                onValueChange = { input ->
                    notifNewText = input
                },
                label = { Text(stringResource(R.string.new_text_label)) },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                updateNotificationText(ctx, notifId, notifNewText)
            }) {
                Text(text = stringResource(R.string.button_update_notification))
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(onClick = {
                clearAllNotifications(ctx)
            }) {
                Text(text = stringResource(R.string.button_delete_all_notifications))
            }
        }
    }
}

private fun updateNotificationText(ctx: Context, idText: String, newText: String) {
    val resManager = ResManager(ctx)
    if (idText.isEmpty()) {
        Toast.makeText(ctx, resManager.getString(R.string.toast_enter_id), Toast.LENGTH_SHORT).show()
        return
    }

    if (newText.isEmpty()) {
        Toast.makeText(ctx, resManager.getString(R.string.toast_enter_new_text), Toast.LENGTH_SHORT).show()
        return
    }

    val notificationId = idText.toIntOrNull()
    if (notificationId == null) {
    Toast.makeText(ctx, resManager.getString(R.string.toast_not_right_id), Toast.LENGTH_SHORT).show()
        return
    }

    val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val activeNotifications = notificationManager.activeNotifications

    val targetNotification = activeNotifications.find { it.id == notificationId }

    if (targetNotification == null) {
        Toast.makeText(ctx, resManager.getString(R.string.toast_not_find_notification_by_id, notificationId), Toast.LENGTH_SHORT).show()
        return
    }

    val originalNotification = targetNotification.notification
    val extras = originalNotification.extras

    val originalTitle = extras.getString(NotificationCompat.EXTRA_TITLE)
        ?: extras.getCharSequence(NotificationCompat.EXTRA_TITLE)?.toString() ?: resManager.getString(R.string.default_title)

    val priority = originalNotification.priority
    val channelId = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        originalNotification.channelId
    } else {
        Keys.DEFAULT_CHANNEL_ID
    }

    val updatedNotification = NotificationCompat.Builder(ctx, channelId)
        .setSmallIcon(R.drawable.ic_notifications_active_24)
        .setContentTitle(originalTitle)
        .setContentText(newText)
        .setStyle(NotificationCompat.BigTextStyle().bigText(newText))
        .setPriority(priority)
        .setAutoCancel(originalNotification.flags and android.app.Notification.FLAG_AUTO_CANCEL != 0)
        .build()

    notificationManager.notify(notificationId, updatedNotification)

    Toast.makeText(ctx, resManager.getString(R.string.toast_update_success, originalTitle), Toast.LENGTH_SHORT).show()
}

private fun clearAllNotifications(ctx: Context) {
    val resManager = ResManager(ctx)
    val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val activeNotifications = notificationManager.activeNotifications

    if (activeNotifications.isEmpty()) {
        Toast.makeText(ctx, resManager.getString(R.string.toast_no_active_notifications), Toast.LENGTH_SHORT).show()
        return
    }
    notificationManager.cancelAll()
}
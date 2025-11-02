package ru.itis.android.utils

import android.content.Context
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.itis.android.Keys
import ru.itis.android.MainActivity
import ru.itis.android.R
import ru.itis.android.model.NotificationModel

//import ru.itis.android.model.NotificationModel
//import ru.itis.android.model.NotificationType

class NotificationHandler(
    private val ctx: Context,
    ) {

    private val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(messageData: NotificationModel) {
        val builder = NotificationCompat.Builder(ctx, "")
            .setSmallIcon(R.drawable.ic_notifications_active_24)
            .setContentTitle(messageData.title)
        messageData.content?.let(builder::setContentText)
        notificationManager.notify(123, builder.build())

    }


    private fun createNotificationChannelIfNeeded() {

    }


    private companion object {
        const val DEFAULT_CHANNEL_ID = "itis_default_channel_id"
    }
}
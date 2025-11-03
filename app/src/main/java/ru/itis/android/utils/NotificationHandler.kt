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
import kotlinx.serialization.builtins.NothingSerializer
import ru.itis.android.Keys
import ru.itis.android.MainActivity
import ru.itis.android.R
import ru.itis.android.model.NotificationModel

//import ru.itis.android.model.NotificationModel
//import ru.itis.android.model.NotificationType

class NotificationHandler(
    private val ctx: Context,
    private val resManager: ResManager
    ) {

    private val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager




    fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelIfNeeded()
        }
    }


    fun showNotification(messageData: NotificationModel) {
        val baseInteger = Intent(ctx, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(Keys.INTENT_KEY, "Sample payload")
        }

        val baseInteger2 = Intent(ctx, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(Keys.EXTRA_PAYLOAD_KEY, 123)
        }

//        val pendingIntent = PendingIntent.getActivity(
//            ctx, 412, baseInteger, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )

        val actionIntent = PendingIntent.getActivity(
            ctx, 413, baseInteger2, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val firstAction = NotificationCompat.Action.Builder(
            R.drawable.ic_outline_airline_stops_24,
            resManager.getString(R.string.go_back_button),
            actionIntent
        ).build()

        val builder = NotificationCompat.Builder(ctx, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_active_24)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setContentTitle(messageData.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(firstAction)
            .setAutoCancel(true)

        messageData.content?.let(builder::setContentText)



        notificationManager.notify(messageData.id, builder.build())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelIfNeeded() {
        val channel = NotificationChannel(
            DEFAULT_CHANNEL_ID,
            resManager.getString(R.string.notification_channel_default),
            NotificationManager.IMPORTANCE_HIGH

        )

        notificationManager.createNotificationChannel(channel)
    }


    private companion object {
        const val DEFAULT_CHANNEL_ID = "itis_default_channel_id"
    }
}
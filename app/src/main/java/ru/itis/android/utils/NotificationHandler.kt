package ru.itis.android.utils

import android.content.Context
import android.app.NotificationChannel
import android.app.NotificationChannel.DEFAULT_CHANNEL_ID
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.input.key.Key
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import ru.itis.android.Keys
import ru.itis.android.MainActivity
import ru.itis.android.R
import ru.itis.android.model.NotificationModel
import ru.itis.android.model.ReplyReceiver

class NotificationHandler(
    private val ctx: Context,
    private val resManager: ResManager
    ) {

    private val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var isChannelInitialized = false
    fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelIfNeeded()
            isChannelInitialized = true
            println("TEST TAG: Notification channels initialized")
        } else {
            println("TEST TAG: Notification channels not needed (Android < 8.0)")
            isChannelInitialized = true
        }
    }


    fun showNotification(
        messageData: NotificationModel,

        isOpenableText: Boolean,
        isClickableToMainActivity: Boolean,
        isAnswerable: Boolean
    ) {

        if (!isChannelInitialized) {
            println("TEST TAG: WARNING - Notification channels not initialized! Calling init...")
            initNotificationChannel()
        }

        val pendingIntent = if (isClickableToMainActivity) {
            val mainIntent = Intent(ctx, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(Keys.NOTIFICATION_TITLE, messageData.title)
                putExtra(Keys.NOTIFICATION_CONTENT, messageData.content)
                putExtra(Keys.NOTIFICATION_ID, messageData.id)
            }
            PendingIntent.getActivity(
                ctx,
                messageData.id,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            null
        }

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isChannelInitialized){
            DEFAULT_CHANNEL_ID
        } else {
            null
        }


        val builder = if (channelId != null) {
            NotificationCompat.Builder(ctx, channelId)
        } else {
            NotificationCompat.Builder(ctx)
        }
        builder.setSmallIcon(R.drawable.ic_notifications_active_24)
                .setContentTitle(messageData.title)
                .setPriority(messageData.priority.importance)
                .setAutoCancel(false)



        messageData.content?.let { content ->
            if (isOpenableText) {
                builder.setStyle(NotificationCompat.BigTextStyle().bigText(content))
            } else {
                builder.setContentText(content)
            }

        }

        if (isClickableToMainActivity) {
            pendingIntent?.let(builder::setContentIntent)
        }

        if (isAnswerable) {
            addReplyAction(builder, messageData, messageData.id)
        }

        messageData.content?.let(builder::setContentText)

        notificationManager.notify(messageData.id, builder.build())

    }

    private fun addReplyAction(
        builder: NotificationCompat.Builder,
        messageData: NotificationModel,
        notificationId: Int
    ) {
        val remoteInput = RemoteInput.Builder(Keys.KEY_REPLY)
            .setLabel("Введите ваш ответ...")
            .build()

        val replyIntent = Intent(ctx, ReplyReceiver::class.java).apply {
            putExtra(Keys.NOTIFICATION_ID, notificationId)
            putExtra(Keys.ORIGINAL_TITLE, messageData.title)
            putExtra(Keys.ORIGINAL_TEXT, messageData.content)
        }

        val pendingIntentFlags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            }

            else -> {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }


        val replyPendingIntent = PendingIntent.getBroadcast(
            ctx,
            notificationId + 1000,
            replyIntent,
            pendingIntentFlags
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val replyAction = NotificationCompat.Action.Builder(
                R.drawable.ic_outline_reply_24,
                "Ответить",
                replyPendingIntent
            ).addRemoteInput(remoteInput)
                .build()
            builder.addAction(replyAction)
            println("TEST TAG: Reply action added (Android 7.0+)")

        } else {
            val fallbackAction = NotificationCompat.Action.Builder(
                R.drawable.ic_outline_reply_24,
                "Открыть приложение",
                replyPendingIntent
            ).build()

            builder.addAction(fallbackAction)
            println("TEST TAG: Fallback action added (Android < 7.0)")
        }
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
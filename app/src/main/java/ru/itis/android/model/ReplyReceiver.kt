package ru.itis.android.model

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.itis.android.Keys
import ru.itis.android.MessagesRepository

class ReplyReceiver : BroadcastReceiver() {

    override fun onReceive(ctx: Context?, intent: Intent?) {

        ctx?.let { ctx ->
            val replyText = getReplyText(intent)
            val notificationId: Int = intent?.getIntExtra(Keys.NOTIFICATION_ID, -1)?: -1
            val originalTitle = intent?.getStringExtra(Keys.ORIGINAL_TITLE)
            val originalText = intent?.getStringExtra(Keys.ORIGINAL_TEXT)

            if (!replyText.isNullOrEmpty() && originalTitle != null) {
                MessagesRepository.addMessage(
                    title = originalTitle,
                    text = originalText,
                    answer = replyText
                )
            }

            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.cancel(notificationId)

            if (notificationId != -1) {
                notificationManager.cancel(notificationId)
            }

        }
    }

    private fun getReplyText(intent: Intent?): String? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(Keys.KEY_REPLY)?.toString()
    }
}
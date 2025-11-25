package ru.itis.android.model

import android.app.NotificationManager
import android.content.Context
import ru.itis.android.R

enum class NotificationPriority(
    val importance: Int,
    val stringResId: Int
) {
    LOW(
        importance = NotificationManager.IMPORTANCE_LOW,
        stringResId = R.string.low_priority_displayName
    ),
    MEDIUM(
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        stringResId = R.string.medium_priority_displayName
    ),
    HIGH(
        importance = NotificationManager.IMPORTANCE_HIGH,
        stringResId = R.string.high_priority_displayName
    ),
    URGENT(
        importance = NotificationManager.IMPORTANCE_MAX,
        stringResId = R.string.urgent_priority_displayName
    );

    fun getDisplayName(ctx: Context): String {
        return ctx.getString(stringResId)
    }
}
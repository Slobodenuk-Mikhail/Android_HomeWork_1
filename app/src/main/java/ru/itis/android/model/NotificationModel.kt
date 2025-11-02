package ru.itis.android.model

import androidx.annotation.DrawableRes

data class NotificationModel(
    val id: Int,
    val title: String,
    val content: String? = null,
    @DrawableRes
    val icon: Int?= null
)

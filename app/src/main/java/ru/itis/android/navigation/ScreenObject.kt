package ru.itis.android.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MainPageObject

@Serializable
data object TaskCreatorObject

@Serializable
data class TaskViewerObject(
    val userEmail: String
)
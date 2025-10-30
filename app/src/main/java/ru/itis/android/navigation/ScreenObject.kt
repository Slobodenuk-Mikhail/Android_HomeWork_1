package ru.itis.android.navigation

import kotlinx.serialization.Serializable
import ru.itis.android.model.TaskDataModel


@Serializable
data object MainPageObject


//@Serializable
//data object ViewTasksScreenObject

@Serializable
data object TaskCreatorObject

@Serializable
data class TaskViewerObject(
    val userEmail: String
)
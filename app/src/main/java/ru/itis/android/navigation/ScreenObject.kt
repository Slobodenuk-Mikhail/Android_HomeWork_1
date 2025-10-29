package ru.itis.android.navigation

import kotlinx.serialization.Serializable
import ru.itis.android.model.TaskDataModel


@Serializable
data class MainPageObject (
    val email: String,
//    val password: String,
)

@Serializable
data object MainPageStartObject

//@Serializable
//data object ViewTasksScreenObject


@Serializable
data class ViewTasksObject(
    val taskTitle: String,
    val taskText: String,
    val taskData: TaskDataModel,
)
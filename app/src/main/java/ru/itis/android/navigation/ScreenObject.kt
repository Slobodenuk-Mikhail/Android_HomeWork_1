package ru.itis.android.navigation

import kotlinx.serialization.Serializable
import ru.itis.android.model.TaskDataModel


@Serializable
data object MainPageObject


//@Serializable
//data object ViewTasksScreenObject

@Serializable
data class TaskCreatorObject(
    val userEmail: String,
    val arrayListOfTasks: ArrayList<TaskDataModel> = ArrayList()
)

@Serializable
data class TaskViewerObject(
    val userEmail: String,
    val arrayListOfTasks: ArrayList<TaskDataModel> = ArrayList()
)
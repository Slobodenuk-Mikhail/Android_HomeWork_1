package ru.itis.android.model

import kotlinx.serialization.Serializable


@Serializable
data class TaskDataModel(
    val taskTitle: String,
    val taskText: String,
)
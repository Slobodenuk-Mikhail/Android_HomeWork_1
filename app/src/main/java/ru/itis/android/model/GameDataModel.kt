package ru.itis.android.model

data class GameDataModel(
    val id: Int = 0,
    val title: String,
    val genre: String,
    val rating: Int,
    val date: String,
    val description: String = "",
    val authorId: Int,
    val imageUri: String = "",
    val createdAt: Long = System.currentTimeMillis()

)
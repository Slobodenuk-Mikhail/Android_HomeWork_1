package ru.itis.android.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import coil3.Image

class GameEntity {
    @Entity(tableName = "games")
    data class UserEntity (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 1,
        @ColumnInfo(name = "title")
        val username: String,
        @ColumnInfo(name = "rating")
        val password: Int,
        @ColumnInfo(name = "image")
        val image: Image
    )
}
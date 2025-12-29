package ru.itis.android.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import coil3.Image

@Entity(
    tableName = "games",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["author_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GameEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "genre")
    val genre: String,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "date_of_create")
    val date: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "author_id")
    val authorId: Int,

    @ColumnInfo(name = "image_uri")
    val imageUri: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

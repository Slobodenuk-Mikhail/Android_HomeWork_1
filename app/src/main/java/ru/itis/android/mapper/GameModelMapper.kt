package ru.itis.android.mapper

import ru.itis.android.db.entity.GameEntity
import ru.itis.android.model.GameDataModel

class GameModelMapper {
    fun map(input: GameDataModel): GameEntity {
        return GameEntity(
            title = input.title,
            genre = input.genre,
            rating = input.rating,
            date = input.date,
            description = input.description,
            authorId = input.authorId,
            imageUri = input.imageUri,
            createdAt = input.createdAt
        )
    }

    fun map(input: GameEntity): GameDataModel {
        return GameDataModel(
            id = input.id,
            title = input.title,
            genre = input.genre,
            rating = input.rating,
            date = input.date,
            description = input.description,
            authorId = input.authorId,
            imageUri = input.imageUri,
            createdAt = input.createdAt
        )
    }
}
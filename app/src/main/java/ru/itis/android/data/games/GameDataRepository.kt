package ru.itis.android.data.games

import ru.itis.android.model.GameDataModel

object GameDataRepository {
    private val temporaryGames = mutableListOf<GameDataModel>()

    fun addTemporaryGame(game: GameDataModel) {
        temporaryGames.add(game)
    }

    fun getTemporaryGames(): List<GameDataModel> {
        return temporaryGames.toList()
    }

    fun clearTemporaryGames() {
        temporaryGames.clear()
    }
}
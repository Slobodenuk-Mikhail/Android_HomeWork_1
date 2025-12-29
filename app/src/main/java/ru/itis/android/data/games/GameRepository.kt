package ru.itis.android.data.games

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.android.db.dao.GameDao
import ru.itis.android.mapper.GameModelMapper
import ru.itis.android.model.GameDataModel

class GameRepository(
    private val mapper: GameModelMapper,
    private val ioDispatcher: CoroutineDispatcher,
    private val gameDao: GameDao
) {
    suspend fun createGame(game: GameDataModel): Long {
        return withContext(ioDispatcher) {
            val entity = mapper.map(input = game)
            gameDao.putGame(entity)
        }
    }

    suspend fun getGameById(gameId: Int): GameDataModel? {
        return withContext(ioDispatcher) {
            val entity = gameDao.getGameById(gameId)
            entity?.let { mapper.map(it) }
        }
    }

    suspend fun getGamesByAuthor(authorId: Int): List<GameDataModel> {
        return withContext(ioDispatcher) {
            gameDao.getGamesByAuthorId(authorId).map { mapper.map(it) }
        }
    }

    suspend fun getAllGames(): List<GameDataModel> {
        return withContext(ioDispatcher) {
            gameDao.getAllGames().map { mapper.map(it) }
        }
    }

    suspend fun updateGame(game: GameDataModel) {
        withContext(ioDispatcher) {
            val entity = mapper.map(input = game)
            gameDao.updateGame(entity)
        }
    }

    suspend fun deleteGame(gameId: Int) {
        withContext(ioDispatcher) {
            gameDao.deleteGameById(gameId)
        }
    }

    suspend fun deleteGamesByAuthor(authorId: Int) {
        withContext(ioDispatcher) {
            gameDao.deleteGamesByAuthor(authorId)
        }
    }

    suspend fun refreshUserGames(userId: Int): List<GameDataModel> {
        return withContext(ioDispatcher) {
            gameDao.getGamesByAuthorId(userId).map { mapper.map(it) }
        }
    }
}
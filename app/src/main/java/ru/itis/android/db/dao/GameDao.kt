package ru.itis.android.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.android.db.entity.GameEntity
import ru.itis.android.db.entity.UserEntity

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putGame(game: GameEntity): Long

    @Query("SELECT * FROM games WHERE id = :gameId")
    suspend fun getGameById(gameId: Int): GameEntity?

    @Query("SELECT * FROM games WHERE author_id = :authorId")
    suspend fun getGamesByAuthorId(authorId: Int): List<GameEntity>

    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<GameEntity>

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateGame(game: GameEntity)

    @Delete
    suspend fun deleteGameEntity(game: GameEntity)

    @Query("DELETE FROM games WHERE id = :gameId")
    suspend fun deleteGameById(gameId: Int)

    @Query("DELETE FROM games WHERE author_id = :authorId")
    suspend fun deleteGamesByAuthor(authorId: Int)
}

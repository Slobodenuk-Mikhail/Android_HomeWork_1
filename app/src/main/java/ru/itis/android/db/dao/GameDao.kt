package ru.itis.android.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.android.db.entity.GameEntity

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putGameData(game: GameEntity)

    @Query("SELECT * FROM games WHERE id = :gameId")
    fun getGameData(gameId: Int): GameEntity?

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun updateGameData(game: GameEntity)

    @Delete
    fun deleteGameEntity(game: GameEntity)

    @Query("DELETE FROM games WHERE id = :gameId")
    fun deleteGameById(gameId: Int)
}

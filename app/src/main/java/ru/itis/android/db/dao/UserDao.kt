package ru.itis.android.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.android.db.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun putUserData(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserData(userId: Int): UserEntity?

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateUserData(user: UserEntity)

    @Delete
    suspend fun deleteUserEntity(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun isUserExists(username: String): Int

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUserByLogAndPas(username: String, password: String): UserEntity?

}

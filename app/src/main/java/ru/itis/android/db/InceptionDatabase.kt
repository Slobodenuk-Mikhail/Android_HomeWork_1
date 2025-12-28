package ru.itis.android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.itis.android.db.dao.GameDao
import ru.itis.android.db.dao.UserDao
import ru.itis.android.db.entity.GameEntity
import ru.itis.android.db.entity.UserEntity
import ru.itis.android.db.typeconverter.InceptionConverters

@Database(
    entities = [UserEntity::class, GameEntity::class],
    version = DATABASE_VERSION

)
@TypeConverters(
    InceptionConverters::class
)
abstract class InceptionDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val gameDao: GameDao

}

private const val DATABASE_VERSION = 4
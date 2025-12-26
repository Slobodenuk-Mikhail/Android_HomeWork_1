package ru.itis.android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.itis.android.db.dao.UserDao
import ru.itis.android.db.entity.UserEntity
import ru.itis.android.db.typeconverter.InceptionConverters

@Database(
    entities = [UserEntity::class],
    version = DATABASE_VERSION

)
@TypeConverters(
    InceptionConverters::class
)
abstract class InceptionDatabase : RoomDatabase() {
    abstract val userDao: UserDao

}

private const val DATABASE_VERSION = 2
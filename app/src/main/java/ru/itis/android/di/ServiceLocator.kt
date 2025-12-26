package ru.itis.android.di

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import ru.itis.android.data.UserRepository
import ru.itis.android.db.InceptionDatabase
import ru.itis.android.mapper.UserModelMapper

object ServiceLocator {

    private const val DB_NAME = "inception.db"
    private var inceptionDatabase: InceptionDatabase? = null

    private val userModelMapper = UserModelMapper()
    private var _userRepository : UserRepository? = null

    fun initDatabase(appCtx: Context){
        inceptionDatabase = Room.databaseBuilder(
            appCtx,
            InceptionDatabase::class.java,
            DB_NAME
        ).build()

        _userRepository = UserRepository(
            mapper = userModelMapper,
            ioDispatcher = Dispatchers.IO,
            userDao = getDatabase().userDao
        )
    }

    fun getDatabase() : InceptionDatabase = inceptionDatabase ?: throw IllegalStateException("DB is not initialized")

    fun getUserRepository(): UserRepository = _userRepository ?: throw IllegalStateException("Репозиторий не инициализирован. Сначала вызывете initDatabase()")
}
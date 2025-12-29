package ru.itis.android.di

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import ru.itis.android.data.games.GameRepository
import ru.itis.android.data.users.UserRepository
import ru.itis.android.db.DatabaseMigrations
import ru.itis.android.db.InceptionDatabase
import ru.itis.android.mapper.GameModelMapper
import ru.itis.android.mapper.UserModelMapper

object ServiceLocator {

    private const val DB_NAME = "inception.db"
    private var inceptionDatabase: InceptionDatabase? = null

    private val userModelMapper = UserModelMapper()
    private val gameModelMapper = GameModelMapper()

    private var _userRepository : UserRepository? = null
    private var _gameRepository: GameRepository? = null


    fun initDatabase(appCtx: Context){
        inceptionDatabase = Room.databaseBuilder(
                appCtx,
                InceptionDatabase::class.java,
                DB_NAME
            )
                .build()


        _userRepository = UserRepository(
            mapper = userModelMapper,
            ioDispatcher = Dispatchers.IO,
            userDao = getDatabase().userDao
        )

        _gameRepository = GameRepository(
            mapper = gameModelMapper,
            ioDispatcher = Dispatchers.IO,
            gameDao = getDatabase().gameDao
        )
    }

    fun getDatabase() : InceptionDatabase = inceptionDatabase ?: throw IllegalStateException("DB is not initialized")

    fun getUserRepository(): UserRepository = _userRepository ?: throw IllegalStateException("Репозиторий не инициализирован. Сначала вызывете initDatabase()")

    fun getGameRepository(): GameRepository =
        _gameRepository ?: throw IllegalStateException("Репозиторий игр не инициализирован")
}
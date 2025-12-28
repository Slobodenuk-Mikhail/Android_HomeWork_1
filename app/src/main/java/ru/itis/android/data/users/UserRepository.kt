package ru.itis.android.data.users

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.itis.android.db.dao.UserDao
import ru.itis.android.mapper.UserModelMapper
import ru.itis.android.model.UserDataModel

class UserRepository(
    private val mapper: UserModelMapper,
    private val ioDispatcher: CoroutineDispatcher,
    private val userDao : UserDao
    
) {

    suspend fun createNewUser(userData: UserDataModel) : Int {
        return withContext(ioDispatcher){
            val entity = mapper.map(input = userData)
            val insertId = userDao.putUserData(entity)

            return@withContext insertId.toInt()
        }
    }

    suspend fun isUserExists(username: String) : Boolean {
        return withContext(ioDispatcher){
            userDao.isUserExists(username) > 0
        }
    }

    suspend fun login(username: String, password: String): Int? {
        return withContext(ioDispatcher) {
            val user = userDao.getUserByLogAndPas(username, password)
            user?.id
        }
    }

    suspend fun getUserById(userId: Int): UserDataModel? {
        return withContext(ioDispatcher) {
            val entity = userDao.getUserData(userId)
            entity?.let { mapper.map(it) }
        }
    }

    suspend fun updateUser(userData: UserDataModel) {
        withContext(ioDispatcher) {
            val entity = mapper.map(input = userData)
            userDao.updateUserData(entity)
        }
    }

}
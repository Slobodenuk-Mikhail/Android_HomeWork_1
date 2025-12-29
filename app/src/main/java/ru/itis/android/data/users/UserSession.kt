package ru.itis.android.data.users

import android.content.Context
import android.content.SharedPreferences
import ru.itis.android.Keys

object UserSession {
    private var currentUserId: Int? = null
    private var currentUsername: String? = null
    private var sharedPreferences: SharedPreferences? = null

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(Keys.USER_SESSION, Context.MODE_PRIVATE)
        // Загружаем сохраненные данные при инициализации
        currentUserId = sharedPreferences?.getInt(Keys.USER_ID, -1).takeIf { it != -1 }
        currentUsername = sharedPreferences?.getString(Keys.USERNAME, null)
    }

    fun login(userId: Int, username: String) {
        currentUserId = userId
        currentUsername = username

        // Сохраняем в SharedPreferences
        sharedPreferences?.edit()?.apply {
            putInt(Keys.USER_ID, userId)
            putString(Keys.USERNAME, username)
            apply()
        }
    }

    fun logout() {
        currentUserId = null
        currentUsername = null

        // Очищаем SharedPreferences
        sharedPreferences?.edit()?.apply {
            remove(Keys.USER_ID)
            remove(Keys.USERNAME)
            apply()
        }
    }

    fun isLogged(): Boolean = currentUserId != null
    fun getCurrentUserId(): Int? = currentUserId
    fun getCurrentUsername(): String? = currentUsername
}
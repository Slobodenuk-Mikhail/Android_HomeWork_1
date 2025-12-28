package ru.itis.android.data.users

import android.content.SharedPreferences
import kotlinx.coroutines.delay
import ru.itis.android.Keys
import ru.itis.android.model.UserDataModel

object UserDataRepository {
    private var sharedPref: SharedPreferences? = null

    private val userData = UserDataModel(
        username = "",
        password = ""
    )

    fun isFirstAppLaunch(): Boolean {
        return sharedPref?.getBoolean(Keys.IS_FIRST_APP_LAUNCH, false) ?: false
    }


    fun setIsFirstAppLaunch() {
        sharedPref?.edit()?.putBoolean(Keys.IS_FIRST_APP_LAUNCH, true)?.apply()
    }

    fun provideSharedPrefs(sp: SharedPreferences) {
        if (sharedPref == null) sharedPref = sp
    }

    suspend fun updateUsername(username: String) {
        delay(300L)
        userData.username = username
    }

    suspend fun getCurrentUserData(): UserDataModel {
        delay(1000L)
        return userData
    }
}
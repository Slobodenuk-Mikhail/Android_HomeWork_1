package ru.itis.android

import android.app.Application
import ru.itis.android.data.users.UserDataRepository
import ru.itis.android.di.ServiceLocator

class InceptionApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initDatabase(appCtx = this)
        val sp = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE)
        UserDataRepository.provideSharedPrefs(sp)

    }

    private companion object {
        const val SHARED_PREFS_NAME = "inception_sp"
    }
}
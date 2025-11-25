//package ru.itis.android
//
//import android.content.Context
//import android.content.SharedPreferences
//
//object NotificationSettings {
//    var
//    private const val PREFS_NAME = "notification_settings"
//
//    fun setRedirectEnabled(context: Context, enabled: Boolean) {
//        getPreferences(context).edit()
//            .putBoolean(KEY_REDIRECT_ENABLED, enabled)
//            .apply()
//    }
//
//    private fun getPreferences(ctx: Context): SharedPreferences {
//        return ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//    }
//}
package ru.itis.android.utils

import android.content.Context
import androidx.annotation.StringRes

class ResManager(
    private val ctx: Context
) {
    fun getString(@StringRes stringRes: Int): String {
        return ctx.getString(stringRes)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return ctx.getString(resId, *formatArgs)
    }
}
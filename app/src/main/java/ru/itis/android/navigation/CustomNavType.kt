package ru.itis.android.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.itis.android.model.TaskDataModel

object CustomNavType {

    val TaskDataNavType = object : NavType<TaskDataModel>(isNullableAllowed = true) {

        override fun parseValue(value: String): TaskDataModel {
            return Json.decodeFromString(value)
        }

        override fun serializeAsValue(value: TaskDataModel): String {
            return Json.encodeToString(value)
        }

        override fun get(
            bundle: SavedState,
            key: String
        ): TaskDataModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }



        override fun put(
            bundle: SavedState,
            key: String,
            value: TaskDataModel
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}
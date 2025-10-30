package ru.itis.android.navigation

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.NavType
import androidx.savedstate.SavedState
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

    val ArrayListOfTasksDataNavType = object : NavType<ArrayList<TaskDataModel>>(isNullableAllowed = true) {

        override fun parseValue(value: String): ArrayList<TaskDataModel> {
            return Json.decodeFromString(value)
        }

        override fun serializeAsValue(value: ArrayList<TaskDataModel>): String {
            return Json.encodeToString(value)
        }

        override fun get(
            bundle: Bundle,
            key: String
        ): ArrayList<TaskDataModel>? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: ArrayList<TaskDataModel>
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

    val TaskListSaver = androidx.compose.runtime.saveable.Saver<
            MutableList<TaskDataModel>,
            String
            >(
        save = { list ->
            // Используем твою готовую логику сериализации
            ArrayListOfTasksDataNavType.serializeAsValue(ArrayList(list))
        },
        restore = { saved ->
            // Используем твою готовую логику десериализации
            val arrayList = ArrayListOfTasksDataNavType.parseValue(saved)
            mutableStateListOf<TaskDataModel>().apply {
                addAll(arrayList)
            }
        }
    )
}
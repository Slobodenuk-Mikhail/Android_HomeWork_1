package ru.itis.android.model

import kotlinx.serialization.Serializable


@Serializable
data class UserDataModel(
    val email: String,
    val password: String
)
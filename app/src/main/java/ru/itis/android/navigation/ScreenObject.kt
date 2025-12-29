package ru.itis.android.navigation

import kotlinx.serialization.Serializable

@Serializable
data object CatalogObject {const val route = "catalog"}

@Serializable
data object CreatorObject {const val route = "creator"}

@Serializable
data object ProfileObject {const val route = "profile"}

@Serializable
data object SignInObject {const val route = "singin"}

@Serializable
data object SignUpObject {const val route = "signup"}
package ru.itis.android.data

object UserSession {
    private var currentUserId: Int? = null
    private var currentUsername: String? = null

    fun login(userId: Int, username: String) {
        currentUserId = userId
        currentUsername = username
    }

    fun logout() {
        currentUserId = null
        currentUsername = null
    }

    fun isLogged(): Boolean = currentUserId != null

    fun getCurrentUserId(): Int? = currentUserId
    fun getCurrentUsername(): String? = currentUsername
}
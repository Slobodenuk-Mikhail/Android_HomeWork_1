package ru.itis.android

import androidx.compose.runtime.mutableStateListOf

data class MessageModel(
    val title: String,
    val content: String? = null

)

object MessagesRepository{
    private val _messages = mutableStateListOf<MessageModel>()
    val messages: List<MessageModel> = _messages

    fun addMessage(
        title: String,
        text: String? = null
    ): MessageModel {
        val newMessage = MessageModel(
            title = title,
            content = text
        )
        _messages.add(0, newMessage)
        return newMessage
    }
}

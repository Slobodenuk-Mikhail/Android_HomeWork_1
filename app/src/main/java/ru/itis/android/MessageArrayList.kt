package ru.itis.android

import androidx.compose.runtime.mutableStateListOf

data class MessageModel(
    val title: String,
    val content: String? = null,
    val answer: String? = null

)

object MessagesRepository{
    private val _messages = mutableStateListOf<MessageModel>()
    val messages: List<MessageModel> = _messages

    fun addMessage(
        title: String,
        text: String? = null,
        answer: String? = null
    ): MessageModel {
        val newMessage = MessageModel(
            title = title,
            content = text,
            answer = answer
        )
        _messages.add(0, newMessage)
        return newMessage
    }
}

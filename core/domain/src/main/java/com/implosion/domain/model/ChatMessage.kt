package com.implosion.domain.model

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String,
    val model: String? = null
)

package com.implosion.data.database.entity.note.mapper

import com.implosion.data.remote.model.GenerateResponse
import com.implosion.domain.model.ChatMessage

fun GenerateResponse.toDomain(isFromUser: Boolean = false): ChatMessage {
    return ChatMessage(
        text = response.orEmpty(),
        isFromUser = isFromUser,
        timestamp = "",
        model = model
    )
}
package com.implosion.data.database.entity.tag.mapper

import com.implosion.data.remote.model.transcription.TranscriptionResponse
import com.implosion.domain.model.TranscriptionDomainModel

fun TranscriptionResponse.toDomain(): TranscriptionDomainModel{
    return TranscriptionDomainModel(
        text = text,
    )
}
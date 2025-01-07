package com.implosion.data.database.entity.note.mapper

import com.implosion.data.database.entity.note.NoteEntity
import com.implosion.domain.model.NoteModel

fun NoteModel.toDTO(): NoteEntity {
//    val contentTitle = if (!content.isNullOrEmpty()) {
//        val firstLine = content.split("\n").first()
//        if (firstLine.length > 32) {
//            val words = firstLine.split(" ")
//            val titleBuilder = StringBuilder()
//            for (word in words) {
//                if (titleBuilder.length + word.length + 1 > 32) break
//                if (titleBuilder.isNotEmpty()) titleBuilder.append(" ")
//                titleBuilder.append(word)
//            }
//            titleBuilder.toString()
//        } else {
//            firstLine
//        }
//    } else {
//        ""
//    }
    return NoteEntity(
        id = noteId,
        title = title.orEmpty(),
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
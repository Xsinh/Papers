package com.implosion.data.database.entity.note

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.implosion.data.database.entity.tag.TagEntity

data class NoteWithTags(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(NoteTagCrossRef::class, parentColumn = "noteId", entityColumn = "tagId")
    )
    val tags: List<TagEntity>
)

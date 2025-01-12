package com.implosion.data.database.entity.tag.mapper

import com.implosion.data.database.entity.tag.TagEntity
import com.implosion.domain.model.TagModel

fun TagEntity.toDomain(): TagModel {
    val updatedName = if (name.startsWith("#")) {
        name
    } else if (name.isNotEmpty() && name.isNotBlank()) {
        "#$name"
    } else {
        ""
    }

    return TagModel(
        tagId = id,
        name = updatedName
    )
}

fun TagModel.toDTO(): TagEntity {
    return TagEntity(
        id = this.tagId,
        name = this.name,
    )
}
package com.implosion.data.database.entity.tag

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tag_table")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

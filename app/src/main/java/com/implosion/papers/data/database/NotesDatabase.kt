package com.implosion.papers.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.implosion.papers.data.database.dao.FolderDao
import com.implosion.papers.data.database.dao.NoteDao
import com.implosion.papers.data.database.dao.NoteTagCrossRefDao
import com.implosion.papers.data.database.dao.TagDao
import com.implosion.papers.data.database.entity.folder.FolderEntity
import com.implosion.papers.data.database.entity.note.NoteEntity
import com.implosion.papers.data.database.entity.note.NoteTagCrossRef
import com.implosion.papers.data.database.entity.tag.TagEntity

@Database(
    entities = [
        NoteEntity::class,
        FolderEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    abstract fun folderDao(): FolderDao

    abstract fun tagDao(): TagDao

    abstract fun noteTagCrossRefDao(): NoteTagCrossRefDao
}
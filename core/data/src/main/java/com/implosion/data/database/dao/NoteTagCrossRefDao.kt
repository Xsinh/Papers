package com.implosion.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.implosion.data.database.entity.note.NoteTagCrossRef

@Dao
interface NoteTagCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteTagCrossRef: NoteTagCrossRef)

    @Delete
    suspend fun delete(noteTagCrossRef: NoteTagCrossRef)

    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId AND tagId = :tagId")
    suspend fun deleteByNoteAndTag(noteId: Int, tagId: Int)
}
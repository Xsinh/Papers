package com.implosion.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.implosion.data.database.entity.note.NoteEntity
import com.implosion.data.database.entity.note.NoteTagCrossRef
import com.implosion.data.database.entity.note.NoteWithTags

@Dao
interface NoteTagCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteTagCrossRef: NoteTagCrossRef)

    @Delete
    suspend fun delete(noteTagCrossRef: NoteTagCrossRef)

    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId AND tagId = :tagId")
    suspend fun deleteByNoteAndTag(noteId: Int, tagId: Int)

    @Transaction
    @Query("SELECT * FROM notes_table WHERE id = :noteId")
    suspend fun getTagsForNoteId(noteId: Int): NoteWithTags

    @Transaction
    @Query(
        """
    SELECT * FROM notes_table
    WHERE id IN (
        SELECT noteId FROM NoteTagCrossRef
        INNER JOIN tag_table ON NoteTagCrossRef.tagId = tag_table.id
        WHERE tag_table.name LIKE '%' || :query || '%'
    )
    """
    )
    suspend fun searchTag(query: String): List<NoteWithTags>

    @Transaction
    @Query(
        """
    SELECT * FROM notes_table
    INNER JOIN NoteTagCrossRef ON notes_table.id = NoteTagCrossRef.noteId
    INNER JOIN tag_table ON NoteTagCrossRef.tagId = tag_table.id
    WHERE (tag_table.name = :tagName) AND 
          (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%')
"""
    )
    suspend fun searchNotesByTagAndText(tagName: String, query: String): List<NoteEntity>
}
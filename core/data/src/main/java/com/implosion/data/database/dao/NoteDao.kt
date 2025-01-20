package com.implosion.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.implosion.data.database.entity.note.NoteEntity


@Dao
interface NoteDao {
    @Insert // - юаг блеат
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM notes_table WHERE id = :noteId")
    suspend fun deleteById(noteId: Int)

    @Query("SELECT * FROM notes_table WHERE folderId = :folderId")
    suspend fun getNotesByFolder(folderId: Int?): List<NoteEntity>

    @Query("SELECT * FROM notes_table WHERE folderId IS NULL")
    suspend fun getNotesInRootFolder(): List<NoteEntity>

    @Query("SELECT * FROM notes_table WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity

    @Query("SELECT * FROM notes_table")
    suspend fun getAllNotes(): List<NoteEntity>

    @Query("SELECT * FROM notes_table WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    suspend fun searchNotesByText(query: String): List<NoteEntity>
}
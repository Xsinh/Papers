package com.implosion.papers.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.implosion.papers.data.database.entity.note.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes_table WHERE folderId = :folderId")
    suspend fun getNotesByFolder(folderId: Int?): List<NoteEntity>

    @Query("SELECT * FROM notes_table WHERE folderId IS NULL")
    suspend fun getNotesInRootFolder(): List<NoteEntity>

    //Надо подумать как это сделать при dynamic delivery
    @Query("SELECT * FROM notes_table WHERE folderId = :folderId")
    suspend fun getNotesByFolder(folderId: Int): List<NoteEntity>

    @Query("SELECT * FROM notes_table")
    suspend fun getAllNotes(): List<NoteEntity>
}
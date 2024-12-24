package com.implosion.papers.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.implosion.papers.data.database.entity.folder.FolderEntity
import com.implosion.papers.data.database.entity.folder.FolderWithNotes

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: FolderEntity): Long

    @Update
    suspend fun update(folder: FolderEntity)

    @Delete
    suspend fun delete(folder: FolderEntity)


    //
    @Transaction
    @Query("SELECT * FROM folder_table WHERE id = :folderId")
    suspend fun getFolderWithNotes(folderId: Int): FolderWithNotes
}
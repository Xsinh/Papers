package com.implosion.data.database.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.implosion.data.database.NotesDatabase
import com.implosion.data.remote.api.summarization.OllamaApi
import com.implosion.data.remote.api.summarization.OllamaApiImpl
import com.implosion.data.remote.api.transcription.TranscriptionApi
import com.implosion.data.remote.api.transcription.TranscriptionApiImpl
import org.koin.dsl.module

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 1. Создаем новую таблицу с обновленным полем isMarkedAsComplete
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS notes_table_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                content TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                folderId INTEGER, -- Поле "folderId" может быть NULL
                isMarkedAsComplete INTEGER NOT NULL DEFAULT 0
            )
            """
        )

        // 2. Копируем данные из старой таблицы в новую
        database.execSQL(
            """
            INSERT INTO notes_table_new (title, content, createdAt, updatedAt, folderId, isMarkedAsComplete)
SELECT title, content, createdAt, updatedAt, folderId, 0 FROM notes_table
            """
        )

        // 3. Удаляем старую таблицу
        database.execSQL("DROP TABLE notes_table")

        // 4. Переименовываем новую таблицу в старое имя
        database.execSQL("ALTER TABLE notes_table_new RENAME TO notes_table")
    }
}
val databaseModule = module {
    single(createdAtStart = true) {
        Room.databaseBuilder(
            context = get(),
            klass = NotesDatabase::class.java,
            name = "capital-database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single {
        get<NotesDatabase>().noteDao()
        get<NotesDatabase>().folderDao()
        get<NotesDatabase>().tagDao()
        get<NotesDatabase>().noteTagCrossRefDao()
    }

    single<OllamaApi> { OllamaApiImpl(get()) }

    single<TranscriptionApi> { TranscriptionApiImpl(get()) }
}
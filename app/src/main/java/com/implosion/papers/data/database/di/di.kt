package com.implosion.papers.data.database.di

import androidx.room.Room
import com.implosion.papers.data.database.NotesDatabase
import org.koin.dsl.module

val databaseModule = module {
    single(createdAtStart = true) {
        Room.databaseBuilder(
            context = get(),
            klass = NotesDatabase::class.java,
            name = "capital-database"
        ).build()
    }

    single {
        get<NotesDatabase>().noteDao()
        get<NotesDatabase>().folderDao()
        get<NotesDatabase>().tagDao()
        get<NotesDatabase>().noteTagCrossRefDao()
    }
}
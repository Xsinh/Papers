package com.implosion.papers.data.repository.di

import com.implosion.papers.data.repository.NoteRepositoryImpl
import com.implosion.papers.domain.repository.NoteRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NoteRepository> {
        NoteRepositoryImpl(database = get())
    }
}
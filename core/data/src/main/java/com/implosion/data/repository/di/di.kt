package com.implosion.data.repository.di

import com.implosion.data.repository.HashTagRepositoryImpl
import com.implosion.data.repository.NoteRepositoryImpl
import com.implosion.domain.repository.HashTagRepository
import com.implosion.domain.repository.NoteRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NoteRepository> {
        NoteRepositoryImpl(database = get())
    }
    single<HashTagRepository> {
        HashTagRepositoryImpl(database = get())
    }
}
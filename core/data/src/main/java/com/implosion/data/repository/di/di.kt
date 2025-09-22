package com.implosion.data.repository.di

import com.implosion.data.repository.AudioRepositoryImpl
import com.implosion.data.repository.ChatRepositoryImpl
import com.implosion.data.repository.HashTagRepositoryImpl
import com.implosion.data.repository.NoteRepositoryImpl
import com.implosion.data.repository.SearchRepositoryImpl
import com.implosion.data.repository.TranscriptionRepositoryImpl
import com.implosion.domain.repository.summarization.ChatRepository
import com.implosion.domain.repository.papers.HashTagRepository
import com.implosion.domain.repository.papers.NoteRepository
import com.implosion.domain.repository.papers.SearchRepository
import com.implosion.domain.repository.transcription.AudioRepository
import com.implosion.domain.repository.transcription.TranscriptionRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NoteRepository> {
        NoteRepositoryImpl(database = get())
    }
    single<HashTagRepository> {
        HashTagRepositoryImpl(database = get())
    }
    single<SearchRepository> {
        SearchRepositoryImpl(database = get())
    }
    single<ChatRepository> {
        ChatRepositoryImpl(apiService = get())
    }
    single<TranscriptionRepository> {
        TranscriptionRepositoryImpl(transcriptionApi = get())
    }

    single<AudioRepository> {
        AudioRepositoryImpl(audioRecorderDataSource = get())
    }
}
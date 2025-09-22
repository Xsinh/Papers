package com.implosion.papers.presentation.ui.screen.di

import com.implosion.papers.presentation.use_case.GetAmplitudeUseCase
import com.implosion.papers.presentation.interactor.NoteInteractor
import com.implosion.papers.presentation.interactor.NoteInteractorImpl
import com.implosion.papers.presentation.use_case.PauseRecordingUseCase
import com.implosion.papers.presentation.use_case.ResumeRecordingUseCase
import com.implosion.papers.presentation.interactor.SendMessageUseCase
import com.implosion.papers.presentation.interactor.SendMessageUseCaseImpl
import com.implosion.papers.presentation.use_case.StartRecordingUseCase
import com.implosion.papers.presentation.use_case.StopRecordingUseCase
import com.implosion.papers.presentation.ui.screen.details.NoteDetailsViewModel
import com.implosion.papers.presentation.ui.screen.main.MainViewModel
import com.implosion.papers.presentation.ui.screen.voice.VoiceViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(
            noteInteractor = get(),
            shakePreferenceService = get(),
            sendMessageUseCase = get(),
        )
    }
    viewModel {
        NoteDetailsViewModel(noteRepository = get(), sendMessageUseCase = get())
    }
    viewModel {
        VoiceViewModel(
            transcriptionRepository = get(),
            startRecordingUseCase = get(),
            pauseRecordingUseCase = get(),
            resumeRecordingUseCase = get(),
            stopRecordingUseCase = get(),
            getAmplitudeUseCase = get(),
        )
    }

    factory<NoteInteractor> {
        NoteInteractorImpl(
            noteRepository = get(),
            hashTagRepository = get(),
            searchRepository = get()
        )
    }

    factory<SendMessageUseCase> {
        SendMessageUseCaseImpl(
            repository = get()
        )
    }

    factory {
        StartRecordingUseCase(repository = get())
    }

    factory {
        StopRecordingUseCase(repository = get())
    }

    factory {
        PauseRecordingUseCase(repository = get())
    }

    factory {
        ResumeRecordingUseCase(repository = get())
    }

    factory {
        GetAmplitudeUseCase(repository = get())
    }
}
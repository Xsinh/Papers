package com.implosion.papers.presentation.ui.screen.di

import com.implosion.papers.presentation.interactor.NoteInteractor
import com.implosion.papers.presentation.interactor.NoteInteractorImpl
import com.implosion.papers.presentation.ui.screen.details.NoteDetailsViewModel
import com.implosion.papers.presentation.ui.screen.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(
            noteInteractor = get(),
            shakePreferenceService = get()
        )
    }
    viewModel {
        NoteDetailsViewModel(noteRepository = get())
    }

    factory<NoteInteractor>{
        NoteInteractorImpl(
            noteRepository = get(),
            hashTagRepository = get(),
            searchRepository = get()
        )
    }
}
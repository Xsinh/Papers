package com.implosion.papers.presentation.ui.screen.di

import com.implosion.papers.presentation.ui.screen.details.NoteDetailsViewModel
import com.implosion.papers.presentation.ui.screen.main.MainViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val viewModelModule = module {
    viewModel {
        MainViewModel(noteRepository = get(), hashTagRepository = get())
    }
    viewModel {
        NoteDetailsViewModel(noteRepository = get())
    }
}
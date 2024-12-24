package com.implosion.papers.di

import com.implosion.papers.data.database.di.databaseModule
import com.implosion.papers.data.repository.di.repositoryModule
import com.implosion.papers.presentation.provider.providerModule
import com.implosion.papers.presentation.ui.screen.di.viewModelModule

val modulesList = listOf(
    viewModelModule,
    databaseModule,
    repositoryModule,
    providerModule,
)
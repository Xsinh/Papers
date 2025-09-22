package com.implosion.papers.di

import com.implosion.data.database.di.databaseModule
import com.implosion.data.repository.di.repositoryModule
import com.implosion.data.source.di.audioDataSourceModule
import com.implosion.network.di.networkModule
import com.implosion.papers.presentation.provider.sharedPreferenceProviderModule
import com.implosion.papers.presentation.ui.screen.di.viewModelModule

val modulesList = listOf(
    networkModule,
    viewModelModule,
    databaseModule,
    repositoryModule,
    sharedPreferenceProviderModule,
    audioDataSourceModule,
)
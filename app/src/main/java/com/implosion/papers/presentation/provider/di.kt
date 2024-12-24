package com.implosion.papers.presentation.provider

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val providerModule = module {
    single { ResourceProvider(androidContext()) }
}
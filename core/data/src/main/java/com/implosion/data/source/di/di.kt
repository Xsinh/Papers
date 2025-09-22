package com.implosion.data.source.di

import com.implosion.data.source.AudioRecorderDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val audioDataSourceModule = module {

    single {
        AudioRecorderDataSource(androidContext())
    }
}
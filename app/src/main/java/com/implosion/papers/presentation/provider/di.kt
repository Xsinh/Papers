package com.implosion.papers.presentation.provider

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPreferenceProviderModule = module {
    single { SharedPreferenceProvider(androidContext()) }
    single { SharedPreferenceShakeService(prefsProvider = get()) }
}
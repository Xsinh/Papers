package com.implosion.network.di

import com.implosion.network.NetworkClient
import org.koin.dsl.module

val networkModule = module {

    // HttpClient
    single {
        NetworkClient
    }

}
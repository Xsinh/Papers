package com.implosion.papers

import android.app.Application
import com.implosion.papers.di.modulesList
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(modulesList)
        }
    }
}
package com.implosion.feature.paint.api

import org.koin.core.module.Module

interface DynamicFeatureModuleApi {

    fun loadModule(): Module
}
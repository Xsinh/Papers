package com.implosion.paint

import com.implosion.feature.paint.api.DynamicFeatureModuleApi
import org.koin.core.module.Module

class DynamicFeatureModuleImpl : DynamicFeatureModuleApi{

    override fun loadModule(): Module = paintDynamicFeatureModule
}
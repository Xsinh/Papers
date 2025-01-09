package com.implosion.paint

import com.implosion.feature.paint.api.PaintFeatureApi
import org.koin.dsl.module


val paintDynamicFeatureModule = module {
    single<PaintFeatureApi> { PaintFeatureApiImpl() }
}
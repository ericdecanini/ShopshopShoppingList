package com.ericthecoder.shopshopshoppinglist.di.module.dependencies

import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android.AndroidModule
import dagger.Module

@Module(
    includes = [
        AndroidModule::class,
    ]
)
class DependenciesModule
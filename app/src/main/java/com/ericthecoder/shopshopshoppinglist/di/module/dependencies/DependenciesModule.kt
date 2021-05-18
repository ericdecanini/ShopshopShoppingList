package com.ericthecoder.shopshopshoppinglist.di.module.dependencies

import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android.AndroidModule
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.chaquopy.ChaquopyModule
import dagger.Module

@Module(includes = [
  ChaquopyModule::class,
  AndroidModule::class
])
class DependenciesModule

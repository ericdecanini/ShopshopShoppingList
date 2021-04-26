package com.ericdecanini.shopshopshoppinglist.di.module.dependencies

import com.ericdecanini.shopshopshoppinglist.di.module.dependencies.android.AndroidModule
import com.ericdecanini.shopshopshoppinglist.di.module.dependencies.chaquopy.ChaquopyModule
import dagger.Module

@Module(includes = [
  ChaquopyModule::class,
  AndroidModule::class
])
class DependenciesModule

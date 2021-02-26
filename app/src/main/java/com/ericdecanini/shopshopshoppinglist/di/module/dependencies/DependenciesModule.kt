package com.ericdecanini.shopshopshoppinglist.di.module.dependencies

import com.ericdecanini.shopshopshoppinglist.di.module.dependencies.chaquopy.ChaquopyModule
import com.ericdecanini.shopshopshoppinglist.di.module.dependencies.resource.ResourceModule
import dagger.Module

@Module(includes = [
  ChaquopyModule::class,
  ResourceModule::class
])
class DependenciesModule

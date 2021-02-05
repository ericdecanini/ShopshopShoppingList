package com.ericdecanini.shopshopshoppinglist.di.module.dependencies.chaquopy

import com.ericdecanini.shopshopshoppinglist.dependencies.chaquopy.ChaquopyDatabaseWrapper
import com.ericdecanini.shopshopshoppinglist.usecases.database.PythonDatabaseWrapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ChaquopyModule {

  @Singleton
  @Provides
  fun providePythonDatabaseWrapper(): PythonDatabaseWrapper = ChaquopyDatabaseWrapper()

}

package com.ericdecanini.shopshopshoppinglist.di.module.dependencies.chaquopy

import android.content.Context
import com.ericdecanini.shopshopshoppinglist.dependencies.chaquopy.ChaquopyDatabaseWrapper
import com.ericdecanini.shopshopshoppinglist.dependencies.chaquopy.ChaquopyInitializer
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.ericdecanini.shopshopshoppinglist.usecases.python.PythonInitializer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ChaquopyModule {

  @Singleton
  @Provides
  fun providePythonDatabaseWrapper(): PythonDatabaseWrapper = ChaquopyDatabaseWrapper()

  @Singleton
  @Provides
  fun providePythonInitializer(context: Context): PythonInitializer = ChaquopyInitializer(context)

}

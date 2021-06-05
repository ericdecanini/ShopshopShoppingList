package com.ericthecoder.shopshopshoppinglist.di.module.dependencies.chaquopy

import android.content.Context
import com.ericthecoder.shopshopshoppinglist.dependencies.chaquopy.ChaquopyDatabaseWrapper
import com.ericthecoder.shopshopshoppinglist.dependencies.chaquopy.ChaquopyInitializer
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonDatabaseWrapper
import com.ericthecoder.shopshopshoppinglist.usecases.initializer.PythonInitializer
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

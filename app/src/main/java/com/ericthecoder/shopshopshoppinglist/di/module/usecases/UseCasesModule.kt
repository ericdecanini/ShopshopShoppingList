package com.ericthecoder.shopshopshoppinglist.di.module.usecases

import com.ericthecoder.shopshopshoppinglist.usecases.initializer.AppInitializer
import com.ericthecoder.shopshopshoppinglist.usecases.initializer.PythonInitializer
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun provideAppInitializer(
        pythonInitializer: PythonInitializer
    ): AppInitializer = AppInitializer(
        pythonInitializer
    )
}

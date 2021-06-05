package com.ericthecoder.shopshopshoppinglist.di.module.usecases

import com.ericthecoder.shopshopshoppinglist.usecases.initializer.AppInitializer
import com.ericthecoder.shopshopshoppinglist.usecases.initializer.BillingClientInitializer
import com.ericthecoder.shopshopshoppinglist.usecases.python.PythonInitializer
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun provideAppInitializer(
        billingClientInitializer: BillingClientInitializer,
        pythonInitializer: PythonInitializer
    ): AppInitializer = AppInitializer(billingClientInitializer, pythonInitializer)
}

package com.ericdecanini.shopshopshoppinglist.di.module

import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProviderImpl
import dagger.Module
import dagger.Provides

@Module(
    includes = []
)
class AppModule {

    @Provides
    fun provideViewStateProvider(): ViewStateProvider = ViewStateProviderImpl()

}

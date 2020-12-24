package com.ericdecanini.shopshopshoppinglist.di.module

import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import dagger.Module
import dagger.Provides

@Module(
    includes = []
)
class AppModule {

    @Provides
    fun provideViewStateProvider() = ViewStateProvider()

}

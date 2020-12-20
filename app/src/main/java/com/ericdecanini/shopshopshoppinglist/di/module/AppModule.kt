package com.ericdecanini.shopshopshoppinglist.di.module

import com.ericdecanini.shopshopshoppinglist.di.module.fragment.HomeModule
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        HomeModule::class
    ]
)
class AppModule {

    @Provides
    fun provideSomeString(): String = "Hello World"

}

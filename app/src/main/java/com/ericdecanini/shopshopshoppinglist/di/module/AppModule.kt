package com.ericdecanini.shopshopshoppinglist.di.module

import com.ericdecanini.shopshopshoppinglist.di.module.activity.ActivityBuildersModule
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProviderImpl
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        ViewModelFactoryModule::class
    ]
)
class AppModule {

    @Provides
    fun provideViewStateProvider(): ViewStateProvider = ViewStateProviderImpl()

}

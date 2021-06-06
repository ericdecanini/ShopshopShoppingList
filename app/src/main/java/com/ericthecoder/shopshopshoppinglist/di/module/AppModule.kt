package com.ericthecoder.shopshopshoppinglist.di.module

import com.ericthecoder.dependencies.android.activity.TopActivityProvider
import com.ericthecoder.dependencies.android.activity.TopActivityProviderImpl
import com.ericthecoder.shopshopshoppinglist.di.module.activity.ActivityBuildersModule
import com.ericthecoder.shopshopshoppinglist.util.providers.*
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        ViewModelFactoryModule::class
    ]
)
class AppModule {

    @Singleton
    @Provides
    fun provideViewStateProvider(): ViewStateProvider = ViewStateProviderImpl()

    @Singleton
    @Provides
    fun provideTopActivityProvider(): TopActivityProvider = TopActivityProviderImpl()

    @Singleton
    @Provides
    fun provideCoroutineContextProvider(): CoroutineContextProvider = CoroutineContextProviderImpl()
}

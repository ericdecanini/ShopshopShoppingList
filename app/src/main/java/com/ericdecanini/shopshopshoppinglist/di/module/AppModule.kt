package com.ericdecanini.shopshopshoppinglist.di.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.ericdecanini.shopshopshoppinglist.di.module.activity.ActivityBuildersModule
import com.ericdecanini.shopshopshoppinglist.util.*
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
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideResources(context: Context): Resources = context.resources

    @Singleton
    @Provides
    fun provideCoroutineContextProvider(): CoroutineContextProvider = CoroutineContextProviderImpl()

}

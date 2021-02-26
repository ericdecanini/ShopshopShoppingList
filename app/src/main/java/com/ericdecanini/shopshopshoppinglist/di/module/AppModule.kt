package com.ericdecanini.shopshopshoppinglist.di.module

import android.app.Application
import android.content.Context
import com.ericdecanini.shopshopshoppinglist.di.module.activity.ActivityBuildersModule
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProviderImpl
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
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideResources(context: Context) = context.resources

}

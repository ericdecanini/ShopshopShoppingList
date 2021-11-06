package com.ericthecoder.shopshopshoppinglist.di.module

import com.ericthecoder.shopshopshoppinglist.di.module.activity.ActivityBuildersModule
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android.AndroidModule
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.chaquopy.ChaquopyModule
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.CoroutineContextProviderImpl
import com.ericthecoder.shopshopshoppinglist.util.providers.ViewStateProvider
import com.ericthecoder.shopshopshoppinglist.util.providers.ViewStateProviderImpl
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        ViewModelFactoryModule::class,
        ChaquopyModule::class,
        AndroidModule::class,
    ]
)
class AppModule {

    @Singleton
    @Provides
    fun provideViewStateProvider(): ViewStateProvider = ViewStateProviderImpl()

    @Singleton
    @Provides
    fun provideCoroutineContextProvider(): CoroutineContextProvider = CoroutineContextProviderImpl()
}

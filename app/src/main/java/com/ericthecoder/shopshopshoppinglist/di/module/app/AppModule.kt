package com.ericthecoder.shopshopshoppinglist.di.module.app

import android.content.Context
import androidx.room.Room
import com.ericthecoder.dependencies.android.room.ShopshopDatabase
import com.ericthecoder.shopshopshoppinglist.di.module.app.activity.ActivityBuildersModule
import com.ericthecoder.shopshopshoppinglist.di.module.app.theme.ThemeModule
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android.AndroidModule
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
        ThemeModule::class,
        ViewModelFactoryModule::class,
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

    @Singleton
    @Provides
    fun provideDatabase(context: Context): ShopshopDatabase = Room.databaseBuilder(
        context,
        ShopshopDatabase::class.java,
        "shopshop-db"
    ).build()
}

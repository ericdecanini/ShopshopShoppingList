package com.ericdecanini.shopshopshoppinglist.di

import android.app.Application
import com.ericdecanini.shopshopshoppinglist.ShopshopApplication
import com.ericdecanini.shopshopshoppinglist.di.module.activity.ActivityBuildersModule
import com.ericdecanini.shopshopshoppinglist.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class]
)
interface AppComponent : AndroidInjector<ShopshopApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}

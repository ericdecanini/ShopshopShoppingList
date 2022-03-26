package com.ericthecoder.shopshopshoppinglist.di

import android.app.Application
import com.ericthecoder.shopshopshoppinglist.ShopshopApplication
import com.ericthecoder.shopshopshoppinglist.di.module.app.AppModule
import com.ericthecoder.shopshopshoppinglist.di.module.dataaccess.DataAccessModule
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.DependenciesModule
import com.ericthecoder.shopshopshoppinglist.di.module.usecases.UseCasesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DependenciesModule::class,
        DataAccessModule::class,
        UseCasesModule::class
    ]
)
interface AppComponent : AndroidInjector<ShopshopApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}

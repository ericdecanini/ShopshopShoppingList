package com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android

import android.app.Application
import android.content.Context
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android.resource.ResourceModule
import com.ericthecoder.shopshopshoppinglist.di.module.dependencies.android.sharedprefs.SharedPrefsModule
import dagger.Module
import dagger.Provides

@Module(includes = [
    ResourceModule::class,
    SharedPrefsModule::class
])
class AndroidModule {

    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

}

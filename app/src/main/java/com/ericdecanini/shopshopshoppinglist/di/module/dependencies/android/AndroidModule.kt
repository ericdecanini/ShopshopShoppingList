package com.ericdecanini.shopshopshoppinglist.di.module.dependencies.android

import com.ericdecanini.shopshopshoppinglist.di.module.dependencies.android.resource.ResourceModule
import com.ericdecanini.shopshopshoppinglist.di.module.dependencies.android.sharedprefs.SharedPrefsModule
import dagger.Module

@Module(includes = [
    ResourceModule::class,
    SharedPrefsModule::class
])
class AndroidModule

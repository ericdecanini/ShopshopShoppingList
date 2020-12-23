package com.ericdecanini.shopshopshoppinglist.di.module.activity

import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [MainModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}

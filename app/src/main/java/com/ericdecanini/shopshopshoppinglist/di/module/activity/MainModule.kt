package com.ericdecanini.shopshopshoppinglist.di.module.activity

import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigatorImpl
import dagger.Module
import dagger.Provides

@Module(includes = [MainFragmentsModule::class])
class MainModule {

    @Provides
    fun provideMainNavigator(mainActivity: MainActivity): MainNavigator {
        return MainNavigatorImpl(mainActivity)
    }

}

package com.ericdecanini.shopshopshoppinglist.di.module.activity

import androidx.appcompat.app.AppCompatActivity
import com.ericdecanini.shopshopshoppinglist.di.module.dialog.UiModule
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigatorImpl
import com.ericdecanini.shopshopshoppinglist.util.TopActivityProvider
import dagger.Module
import dagger.Provides

@Module(includes = [
    MainFragmentsModule::class,
    UiModule::class
])
class MainModule {

    @Provides
    fun provideMainActivity(activity: MainActivity): AppCompatActivity = activity

    @Provides
    fun provideMainNavigator(topActivityProvider: TopActivityProvider): MainNavigator
            = MainNavigatorImpl(topActivityProvider)

}

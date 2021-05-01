package com.ericdecanini.shopshopshoppinglist.di.module.activity.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.di.ViewModelKey
import com.ericdecanini.shopshopshoppinglist.di.module.activity.ActivityModule
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigatorImpl
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainViewModel
import com.ericdecanini.shopshopshoppinglist.util.navigator.Navigator
import com.ericdecanini.shopshopshoppinglist.util.providers.TopActivityProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [
    MainFragmentsModule::class,
    ActivityModule::class,
    MainModule.ViewModelModule::class
])
class MainModule {

    @Provides
    fun provideActivity(activity: MainActivity): AppCompatActivity = activity

    @Provides
    fun provideMainNavigator(
        navigator: Navigator,
        topActivityProvider: TopActivityProvider
    ): MainNavigator = MainNavigatorImpl(navigator, topActivityProvider)

    @Module
    internal abstract class ViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        internal abstract fun bindMainViewModule(viewModel: MainViewModel): ViewModel
    }
}

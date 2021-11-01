package com.ericthecoder.shopshopshoppinglist.di.module.activity.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.ericthecoder.dependencies.android.activity.TopActivityProvider
import com.ericthecoder.shopshopshoppinglist.di.ViewModelKey
import com.ericthecoder.shopshopshoppinglist.di.module.activity.ActivityModule
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainActivity
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainNavigatorImpl
import com.ericthecoder.shopshopshoppinglist.mvvm.activity.main.MainViewModel
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
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
        internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
    }
}

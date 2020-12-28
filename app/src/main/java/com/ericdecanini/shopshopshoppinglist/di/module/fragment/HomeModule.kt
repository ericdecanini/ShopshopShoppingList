package com.ericdecanini.shopshopshoppinglist.di.module.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ericdecanini.shopshopshoppinglist.di.ViewModelFactory
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    internal fun provideViewModel(
        mainNavigator: MainNavigator
    ): HomeViewModel = object: ViewModelFactory() {
        override fun create(): ViewModel = HomeViewModel(
            mainNavigator
        )
    }.create(HomeViewModel::class.java)
}

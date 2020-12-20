package com.ericdecanini.shopshopshoppinglist.di.module.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ericdecanini.shopshopshoppinglist.di.ViewModelFactory
import com.ericdecanini.shopshopshoppinglist.home.HomeViewModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    internal fun provideViewModelFactory(): ViewModelProvider.Factory {
        return object: ViewModelFactory() {
            override fun create(): ViewModel = HomeViewModel()
        }
    }
}

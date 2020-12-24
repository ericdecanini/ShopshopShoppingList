package com.ericdecanini.shopshopshoppinglist.di.module.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ericdecanini.shopshopshoppinglist.di.ViewModelFactory
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list.ListViewModel
import com.ericdecanini.shopshopshoppinglist.util.ViewStateProvider
import dagger.Module
import dagger.Provides

@Module
class ListModule {

    @Provides
    internal fun provideViewModelFactory(
        viewStateProvider: ViewStateProvider
    ): ViewModelProvider.Factory {
        return object: ViewModelFactory() {
            override fun create(): ViewModel = ListViewModel(
                viewStateProvider
            )
        }
    }
}

package com.ericdecanini.shopshopshoppinglist.di.module.fragment

import androidx.lifecycle.ViewModel
import com.ericdecanini.shopshopshoppinglist.di.ViewModelKey
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [HomeModule.ViewModelModule::class])
class HomeModule {

    @Module
    internal abstract class ViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(HomeViewModel::class)
        internal abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
    }
}

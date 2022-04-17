package com.ericthecoder.shopshopshoppinglist.di.module.app.fragment

import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.di.module.app.ViewModelKey
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeViewModel
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

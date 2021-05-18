package com.ericthecoder.shopshopshoppinglist.di.module.fragment

import androidx.lifecycle.ViewModel
import com.ericthecoder.shopshopshoppinglist.di.ViewModelKey
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list.ListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ListModule.ViewModelModule::class])
class ListModule {

    @Module
    internal abstract class ViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(ListViewModel::class)
        internal abstract fun bindListViewModel(viewModel: ListViewModel): ViewModel
    }
}

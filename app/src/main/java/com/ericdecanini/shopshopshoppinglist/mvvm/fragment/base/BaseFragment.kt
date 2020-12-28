package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base

import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<V : BaseViewModel>: DaggerFragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(getViewModelClass())
    }

    abstract fun getViewModelClass(): Class<V>

}

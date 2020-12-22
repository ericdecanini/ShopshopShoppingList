package com.ericdecanini.shopshopshoppinglist.base

import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<V : BaseViewModel>: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(getViewModelClass())
    }

    abstract fun getViewModelClass(): Class<V>

}

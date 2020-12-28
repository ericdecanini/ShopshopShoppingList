package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base

import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<V: ViewModel>: DaggerFragment() {

    @Inject
    protected lateinit var viewModel: V
}

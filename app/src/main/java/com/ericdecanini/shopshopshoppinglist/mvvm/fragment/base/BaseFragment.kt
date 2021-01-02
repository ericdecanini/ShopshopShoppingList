package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base

import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<V: BaseViewModel>: DaggerFragment() {

    @Inject
    protected lateinit var viewModel: V

    init {
        setNavController()
    }

    private fun setNavController() {
        viewModel.setControllerForNavigator(findNavController())
    }
}

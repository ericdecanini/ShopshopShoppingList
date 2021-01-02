package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base

import android.content.Context
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<V: BaseViewModel>: DaggerFragment() {

    @Inject
    protected lateinit var viewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setNavController()
    }

    private fun setNavController() {
        viewModel.setControllerForNavigator(findNavController())
    }
}

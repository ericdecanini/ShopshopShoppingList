package com.ericdecanini.shopshopshoppinglist.home

import android.os.Bundle
import android.view.View
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.base.BaseFragment

class HomeFragment : BaseFragment<HomeViewModel>(R.layout.fragment_home) {

    override fun getViewModelClass() = HomeViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

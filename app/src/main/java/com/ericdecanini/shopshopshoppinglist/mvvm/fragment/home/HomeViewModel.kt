package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator
) : BaseViewModel() {

    fun navigateToListFragment(navController: NavController)
            = mainNavigator.goToList(navController)

}

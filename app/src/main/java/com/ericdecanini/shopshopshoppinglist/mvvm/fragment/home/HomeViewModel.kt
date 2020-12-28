package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.ericdecanini.shopshopshoppinglist.mvvm.activity.main.MainNavigator
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val mainNavigator: MainNavigator
) : ViewModel() {

    fun navigateToListFragment(navController: NavController)
            = mainNavigator.goToList(navController)

}

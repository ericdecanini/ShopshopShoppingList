package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.base

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.ericdecanini.shopshopshoppinglist.mvvm.Navigator

abstract class BaseViewModel(
    private val navigator: Navigator?
): ViewModel() {

  constructor(): this(null)

  fun setControllerForNavigator(navController: NavController) {
    navigator?.navController = navController
  }

}

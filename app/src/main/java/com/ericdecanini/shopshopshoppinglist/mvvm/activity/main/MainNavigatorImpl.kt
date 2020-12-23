package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.navigation.NavController
import com.ericdecanini.shopshopshoppinglist.R

class MainNavigatorImpl : MainNavigator {

    override fun goToList(navController: NavController)
            = navController.navigate(R.id.listFragment)
}

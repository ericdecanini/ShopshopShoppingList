package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ericthecoder.shopshopshoppinglist.R
import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.HomeFragmentDirections
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator
import com.ericthecoder.shopshopshoppinglist.util.providers.TopActivityProvider

class MainNavigatorImpl(
    navigator: Navigator,
    private val topActivityProvider: TopActivityProvider
) : MainNavigator, Navigator by navigator {

    private val navController: NavController?
        get() = (topActivityProvider.getTopActivity()
            ?.supportFragmentManager
            ?.findFragmentById(R.id.fragment_container_view) as? NavHostFragment)
            ?.navController

    override fun goToList() {
        val action = HomeFragmentDirections.actionHomeFragmentToListFragment()
        navController?.navigate(action)
    }

    override fun goToList(shoppingList: ShoppingList) {
        val action = HomeFragmentDirections.actionHomeFragmentToListFragment()
        action.shoppingListId = shoppingList.id
        navController?.navigate(action)
    }

    override fun navigateUp() {
        navController?.navigateUp()
    }
}

package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.ericdecanini.shopshopshoppinglist.R
import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.HomeFragmentDirections

class MainNavigatorImpl(
    private val originActivity: AppCompatActivity
) : MainNavigator {

    private val navController: NavController
        get() = originActivity.findNavController(R.id.fragment_container_view)

    override fun goToList() {
        val action = HomeFragmentDirections.actionHomeFragmentToListFragment()
        navController.navigate(action)
    }

    override fun goToList(shoppingList: ShoppingList) {
        val action = HomeFragmentDirections.actionHomeFragmentToListFragment()
        action.shoppingListId = shoppingList.id
        navController.navigate(action)
    }

}

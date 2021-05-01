package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.util.navigator.Navigator

interface MainNavigator : Navigator {

    fun goToList()

    fun goToList(shoppingList: ShoppingList)

    fun navigateUp()

}

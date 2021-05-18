package com.ericthecoder.shopshopshoppinglist.mvvm.activity.main

import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList
import com.ericthecoder.shopshopshoppinglist.util.navigator.Navigator

interface MainNavigator : Navigator {

    fun goToList()

    fun goToList(shoppingList: ShoppingList)

    fun navigateUp()

}

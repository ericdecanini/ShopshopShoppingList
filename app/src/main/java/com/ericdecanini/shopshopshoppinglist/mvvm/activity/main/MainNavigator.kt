package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import com.ericdecanini.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.mvvm.Navigator

abstract class MainNavigator: Navigator() {

    abstract fun goToList()

    abstract fun goToList(shoppingList: ShoppingList)

}

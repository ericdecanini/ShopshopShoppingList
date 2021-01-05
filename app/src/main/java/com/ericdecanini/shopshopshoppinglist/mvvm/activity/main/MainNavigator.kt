package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList

interface MainNavigator {

    fun goToList()

    fun goToList(shoppingList: ShoppingList)

}

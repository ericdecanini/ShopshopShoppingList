package com.ericdecanini.shopshopshoppinglist.mvvm.activity.main

import androidx.navigation.NavController
import com.ericdecanini.entities.ShoppingList

interface MainNavigator {

    fun goToList(navController: NavController)

    fun goToList(shoppingList: ShoppingList, navController: NavController)

}

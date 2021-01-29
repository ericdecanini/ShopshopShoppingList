package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListEventHandler {

  fun onShoppingListClick(shoppingList: ShoppingList)

}

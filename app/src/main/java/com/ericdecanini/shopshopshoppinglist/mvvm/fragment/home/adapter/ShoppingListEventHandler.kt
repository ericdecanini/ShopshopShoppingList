package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home.adapter

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListEventHandler {

  fun onShoppingListClick(shoppingList: ShoppingList)

}

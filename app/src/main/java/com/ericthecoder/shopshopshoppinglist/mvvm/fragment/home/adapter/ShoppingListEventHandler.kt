package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home.adapter

import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

interface ShoppingListEventHandler {

  fun onShoppingListClick(shoppingList: ShoppingList)

}

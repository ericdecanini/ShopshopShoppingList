package com.ericdecanini.shopshopshoppinglist.usecases.viewstate

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.entities.ViewState

data class HomeViewState(
    val shoppingLists: List<ShoppingList> = listOf()
): ViewState {

    fun withShoppingLists(lists: List<ShoppingList>) = copy(shoppingLists = lists)

}

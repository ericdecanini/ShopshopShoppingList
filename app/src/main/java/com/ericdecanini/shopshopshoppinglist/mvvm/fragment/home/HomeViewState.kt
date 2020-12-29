package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import com.ericdecanini.entities.ShoppingList
import com.ericdecanini.entities.ViewState

data class HomeViewState(
    val shoppingLists: List<ShoppingList> = listOf()
): ViewState {

    fun withShoppingLists(lists: List<ShoppingList>) = copy(shoppingLists = lists)

}

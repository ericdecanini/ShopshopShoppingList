package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import com.ericdecanini.entities.ShoppingList
import com.ericdecanini.entities.ViewState

data class HomeViewState(
    val lists: List<ShoppingList> = listOf()
): ViewState {

    fun withLists(lists: List<ShoppingList>) = copy(lists = lists)

}

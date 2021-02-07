package com.ericdecanini.shopshopshoppinglist.usecases.viewstate

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList
import com.ericdecanini.shopshopshoppinglist.entities.ViewState

data class ListViewState(
    val shoppingList: ShoppingList
): ViewState

package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList

sealed class ListViewState {
    object Initial : ListViewState()
    object Loading : ListViewState()
    data class Loaded(val shoppingList: ShoppingList) : ListViewState()
    data class Error(val exception: Throwable) : ListViewState()
}

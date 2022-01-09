package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.list

import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

sealed class ListViewState {
    object Initial : ListViewState()
    object Loading : ListViewState()
    data class Loaded(val shoppingList: ShoppingList) : ListViewState()
    object Error : ListViewState()
}

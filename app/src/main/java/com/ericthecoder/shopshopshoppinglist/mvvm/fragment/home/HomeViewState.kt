package com.ericthecoder.shopshopshoppinglist.mvvm.fragment.home

import com.ericthecoder.shopshopshoppinglist.entities.ShoppingList

sealed class HomeViewState {
    object Initial : HomeViewState()
    object Loading : HomeViewState()
    data class Loaded(val items: List<ShoppingList>, val isPremium: Boolean) : HomeViewState()
    data class Search(val items: List<ShoppingList>) : HomeViewState()
    data class Error(val exception: Throwable) : HomeViewState()
}

package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.home

import com.ericdecanini.shopshopshoppinglist.entities.ShoppingList

sealed class HomeViewState {
    object Initial : HomeViewState()
    object Loading : HomeViewState()
    data class Loaded(val items: List<ShoppingList>) : HomeViewState()
    data class Error(val exception: Throwable) : HomeViewState()
}

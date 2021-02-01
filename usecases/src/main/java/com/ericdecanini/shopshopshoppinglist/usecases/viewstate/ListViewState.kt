package com.ericdecanini.shopshopshoppinglist.usecases.viewstate

import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ViewState

data class ListViewState(
    val title: String = "",
    val list: MutableList<ShopItem> = mutableListOf()
): ViewState

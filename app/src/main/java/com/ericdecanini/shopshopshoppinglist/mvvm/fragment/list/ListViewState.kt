package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import com.ericdecanini.entities.ShopItem

data class ListViewState(
    val list: List<ShopItem> = listOf(),
    val quantityExpanded: Boolean = false
) {

    fun addNewItem(newItem: ShopItem) = copy(
        list = list.toMutableList().apply { add(newItem) }
    )

    fun replaceListItem(oldItem: ShopItem, newItem: ShopItem) = copy(
        list = list.toMutableList().apply { set(list.indexOf(oldItem), newItem) }
    )

}

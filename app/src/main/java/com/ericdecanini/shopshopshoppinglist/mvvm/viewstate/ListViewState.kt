package com.ericdecanini.shopshopshoppinglist.mvvm.viewstate

import com.ericdecanini.entities.ShopItem
import com.ericdecanini.entities.ViewState

data class ListViewState(
    val list: List<ShopItem> = listOf(),
    val quantityExpanded: Boolean = false
): ViewState {

    fun addNewItem(newItem: ShopItem) = copy(
        list = list.toMutableList().apply { add(newItem) }
    )

    fun replaceListItem(oldItem: ShopItem, newItem: ShopItem): ListViewState {
        val index = list.indexOf(oldItem)
        return if (index == -1)
            this
        else copy(
            list = list.toMutableList().apply { set(list.indexOf(oldItem), newItem) }
        )
    }
}

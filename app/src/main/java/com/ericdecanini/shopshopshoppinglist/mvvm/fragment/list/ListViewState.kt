package com.ericdecanini.shopshopshoppinglist.mvvm.fragment.list

import com.ericdecanini.shopshopshoppinglist.entities.ShopItem
import com.ericdecanini.shopshopshoppinglist.entities.ViewState

data class ListViewState(
    val title: String = "",
    val list: List<ShopItem> = listOf()
): ViewState {

    fun addItem(newItem: ShopItem) = copy(
        list = list.toMutableList().apply { add(newItem) }
    )

    fun replaceItem(oldItem: ShopItem, newItem: ShopItem): ListViewState {
        val index = list.indexOf(oldItem)
        return if (index == -1)
            this
        else copy(
            list = list.toMutableList().apply { set(index, newItem) }
        )
    }

    fun deleteItem(item: ShopItem) = copy(
        list = list.toMutableList().apply { remove(item) }
    )
}

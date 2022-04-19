package com.ericthecoder.shopshopshoppinglist.entities

data class ShoppingList(
    val id: Int,
    private var _name: String,
    val items: MutableList<ShopItem> = mutableListOf(),
) {

    val name get() = _name

    fun rename(newName: String) {
        _name = newName
    }
}

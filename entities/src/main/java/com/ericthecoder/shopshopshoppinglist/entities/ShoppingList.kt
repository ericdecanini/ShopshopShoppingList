package com.ericthecoder.shopshopshoppinglist.entities

data class ShoppingList(
    val id: Int,
    val name: String,
    val items: MutableList<ShopItem> = mutableListOf()
)

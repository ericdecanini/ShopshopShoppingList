package com.ericdecanini.entities

data class ShoppingList(
    val id: Int,
    val title: String,
    val items: List<ShopItem>
)

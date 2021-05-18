package com.ericthecoder.shopshopshoppinglist.entities

data class ShopItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var checked: Boolean
)

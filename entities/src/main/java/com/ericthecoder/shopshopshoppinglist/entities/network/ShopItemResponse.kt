package com.ericthecoder.shopshopshoppinglist.entities.network

data class ShopItemResponse(
    val id: Int,
    val name: String,
    val quantity: Int,
    val checked: Int
)

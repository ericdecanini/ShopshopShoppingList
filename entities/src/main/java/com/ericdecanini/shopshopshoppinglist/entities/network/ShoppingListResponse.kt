package com.ericdecanini.shopshopshoppinglist.entities.network

data class ShoppingListResponse(
    val id: Int,
    val name: String,
    val items: List<ShopItemResponse>
)

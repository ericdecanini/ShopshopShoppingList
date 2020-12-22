package com.ericdecanini.entities

data class ShopItem(
    val name: String,
    val quantity: Int,
    val checked: Boolean
) {
    companion object {
        fun newItem(name: String) = ShopItem(name, 1, false)
    }

}

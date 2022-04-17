package com.ericthecoder.shopshopshoppinglist.entities

data class ShopItem(
    var name: String,
    var quantity: Int,
    var checked: Boolean
) {

    companion object {
        fun createNew(name: String) = ShopItem(name, 1, false)
    }
}

package com.ericdecanini.shopshopshoppinglist.entities

import java.util.*

data class ShopItem(
    val id: String,
    var name: String,
    var quantity: Int,
    var checked: Boolean
) {

    constructor(name: String, quantity: Int, checked: Boolean) :
        this(UUID.randomUUID().toString(), name, quantity, checked)

    companion object {
        fun newItem(name: String) = ShopItem(name, 1, false)
    }
}

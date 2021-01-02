package com.ericdecanini.shopshopshoppinglist.entities

import kotlin.math.max

data class ShopItem(
    val name: String,
    val quantity: Int,
    val checked: Boolean
) {
    companion object {
        fun newItem(name: String) = ShopItem(name, 1, false)
    }

    fun withName(name: String) = copy(name = name)

    fun withQuantity(quantity: Int) = copy(quantity = max(quantity, 1))

    fun withChecked(checked: Boolean) = copy(checked = checked)

}
